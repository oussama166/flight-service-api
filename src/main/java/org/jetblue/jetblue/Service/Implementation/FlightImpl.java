package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Repositories.*;
import org.jetblue.jetblue.Service.FlightService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightImpl implements FlightService {

    private final AirportRepo airportRepo;
    private final AirlineRepo airlineRepo;
    private final FlightStatusRepo flightStatusRepo;
    private final AirplaneRepo airplaneRepo;
    private FlightRepo flightRepo;


    @Override
    public Flight setFlight(
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            double price,
            String departure,
            String arrival,
            String airline,
            String airplane,
            String  flightStatus
    ) {

        // getting the info


        Flight flight = new Flight();

        flight.setDeparture(airportRepo.findByCode(departure).orElse(null));
        flight.setArrival(airportRepo.findByCode(arrival).orElse(null));
        flight.setAirline(airlineRepo.findByAirlineCode(airline).orElse(null));
        flight.setStatus(flightStatusRepo.findByStatus(flightStatus).orElse(null));
        flight.setAirplane(airplaneRepo.findByName(airplane).orElse(null));
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        flight.setPrice(price);

        return flightRepo.save(flight);
    }

    @Override
    public Flight getFlight(String numberFlight) {
        return null;
    }

    @Override
    public Flight updateFlight(String numberFlight, Flight flight) {
        return null;
    }
}
