package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Mapper.Flight.FlightMapper;
import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Search.SearchRequest;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.Entity.FlightItinerary;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Service.AdvanceSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AdvanceSearchImpl implements AdvanceSearchService {

    private final FlightRepo flightRepo;


    private static final Logger logger = LoggerFactory.getLogger(AdvanceSearchImpl.class);

    @Override
    public List<FlightResponse> searchFlights(
            SearchRequest searchRequest
    ) {
        System.out.println("Searching flights with parameters: " +
                "departureTime=" + searchRequest.departureTime() +
                ", arrivalTime=" + searchRequest.arrivalTime() +
                ", origin=" + searchRequest.origin() +
                ", destination=" + searchRequest.destination() +
                ", flightStatus=" + searchRequest.flightStatus());

        return flightRepo.findFlightsAdvanced(
                searchRequest.departureTime(),
                searchRequest.arrivalTime(),
                searchRequest.origin(),
                searchRequest.destination(),
                searchRequest.flightStatus()
        ).stream().map(FlightMapper::toFlightResponse).collect(Collectors.toList());
    }

    public List<FlightItinerary> searchItinerary(String origin, String destination, LocalDateTime departureDate) {
        // Search up to 3 legs
        return searchItineraryRecursive(origin, destination, departureDate, 3);
    }

    // Recursive helper method
    private List<FlightItinerary> searchItineraryRecursive(String origin, String destination, LocalDateTime departureDate, int maxLegs) {
        System.out.println(String.format(
                "searchItineraryRecursive called with origin=%s, destination=%s, departureDate=%s, maxLegs=%d",
                origin, destination, departureDate, maxLegs
        ));

        List<FlightItinerary> results = new ArrayList<>();

        if (maxLegs == 0) {
            System.out.println("No legs left to search, returning empty list");
            return results;
        }

        // Step 1: Check direct flights only on first call
        if (maxLegs == 3) {
            List<Flight> directFlights = flightRepo.findFlightsByDeparture_LocationAndArrival_Location(origin, destination);
            if (!directFlights.isEmpty()) {
                results.add(new FlightItinerary(
                        List.of(FlightMapper.toFlightResponse(directFlights.get(0))),
                        directFlights.get(0).getPrice()
                ));
            } else {
                System.out.println("No direct flights found");
            }
        }

        // Step 2: Search for first legs
        System.out.println(String.format("Searching for flights departing from %s after %s", origin, departureDate));
        List<Flight> firstLegs = flightRepo.findFlightsByDeparture_locationAndDepartureTimeAfter(origin, departureDate);

        for (Flight firstLeg : firstLegs) {
            System.out.println(String.format(
                    "Considering flight %s -> %s departing at %s, arriving at %s",
                    firstLeg.getDeparture().getLocation(),
                    firstLeg.getArrival().getLocation(),
                    firstLeg.getDepartureTime(),
                    firstLeg.getArrivalTime()
            ));

            if (firstLeg.getArrival().getLocation().equals(destination)) {
                System.out.println(String.format(
                        "Flight %s -> %s is a direct flight to destination",
                        firstLeg.getDeparture().getLocation(),
                        destination
                ));
                results.add(new FlightItinerary(
                        List.of(FlightMapper.toFlightResponse(firstLeg)),
                        firstLeg.getPrice()
                ));
            } else if (maxLegs > 1) {
                // Debug layover calculation
                System.out.println("Arrival time of first leg: " + firstLeg.getArrivalTime());
                // in cas national flights, we assume a 2-hour to 6hours max layover
                // For international flights, we assume a 2-hour to 24-hour layover
                LocalDateTime nextDepartureDate = firstLeg.getArrivalTime().plusHours(2);
                System.out.println("Next departure search starts from: " + nextDepartureDate);

                List<FlightItinerary> nextLegItineraries = searchItineraryRecursive(
                        firstLeg.getArrival().getLocation(),
                        destination,
                        nextDepartureDate,
                        maxLegs - 1
                );

                for (FlightItinerary nextItin : nextLegItineraries) {
                    List<FlightResponse> combinedFlights = new ArrayList<>();
                    combinedFlights.add(FlightMapper.toFlightResponse(firstLeg));
                    combinedFlights.addAll(nextItin.getFlights());

                    double totalPrice = firstLeg.getPrice() + nextItin.getTotalPrice();

                    System.out.println(String.format(
                            "Combining flights, total legs: %d, total price: %.2f",
                            combinedFlights.size(), totalPrice
                    ));

                    results.add(new FlightItinerary(combinedFlights, totalPrice));
                }
            }
        }

        System.out.println(String.format("Returning %d itineraries from origin %s", results.size(), origin));
        return results;
    }


}


