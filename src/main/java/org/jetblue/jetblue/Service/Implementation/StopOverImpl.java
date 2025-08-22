package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.StopOver.StopOverMapper;
import org.jetblue.jetblue.Mapper.StopOver.StopOverRequest;
import org.jetblue.jetblue.Mapper.StopOver.StopOverResponse;
import org.jetblue.jetblue.Models.DAO.Airport;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.StopOver;
import org.jetblue.jetblue.Repositories.AirportRepo;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Repositories.StopOverRepo;
import org.jetblue.jetblue.Service.StopOverService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StopOverImpl implements StopOverService {

    private final StopOverRepo stopOverRepo;
    private final FlightRepo flightRepo;
    private final AirportRepo airportRepo;

    @Override
    public List<StopOverResponse> getStopOverDetails(String flightNumber) {
        return stopOverRepo.findByFlight_FlightNumber(flightNumber)
                .map(stopOvers -> stopOvers.stream()
                        .map(StopOverMapper::toStopOverResponse)
                        .toList())
                .orElse(List.of());
    }

    @Override
    public void addStopOver(String flightId, String airportId, StopOverRequest stopOverDetails) {
        if (flightId == null && airportId == null) {
            throw new IllegalArgumentException("Flight ID and Airport ID cannot both be null");
        }

        if (stopOverDetails == null) {
            throw new IllegalArgumentException("StopOver details cannot be null");
        }
        try {
            Flight flight = flightRepo.findByFlightNumber(flightId)
                    .orElseThrow(() -> new IllegalArgumentException("Flight not found with ID: " + flightId));
            Airport airport = airportRepo.findByCode(airportId).
                    orElseThrow(() -> new IllegalArgumentException("Airport not found with ID: " + airportId));
            StopOver stopOver = StopOver.builder()
                    .airport(airport)
                    .flight(flight)
                    .stopOrder(stopOverDetails.stopOrder())
                    .arrivalTime(stopOverDetails.arrivalTime())
                    .departureTime(stopOverDetails.departureTime())
                    .build();
            stopOverRepo.save(stopOver);
        } catch (Exception e) {
            throw new RuntimeException("Error adding stopover: " + e.getMessage(), e);
        }

    }

    @Override
    public void reorderStopOver(String flightId, int oldIndex, int newIndex) {
        if (flightId == null || oldIndex == newIndex) {
            throw new IllegalArgumentException("Flight ID cannot be null and oldIndex must not equal newIndex");
        }
        List<StopOver> stopOvers = stopOverRepo.findByFlight_FlightNumber(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with ID: " + flightId));

        StopOver oldStop = stopOvers.stream()
                .filter(s -> s.getStopOrder() == oldIndex)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("StopOver with oldIndex not found"));
        StopOver newStop = stopOvers.stream()
                .filter(s -> s.getStopOrder() == newIndex)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("StopOver with newIndex not found"));

        // Use a temporary value to avoid unique constraint violation
        oldStop.setStopOrder(-1);
        stopOverRepo.save(oldStop);

        newStop.setStopOrder(oldIndex);
        stopOverRepo.save(newStop);

        oldStop.setStopOrder(newIndex);
        stopOverRepo.save(oldStop);
    }

    @Override
    public void deleteStopOver(String flightId, int stopOrderIndex) {
        if (flightId == null || stopOrderIndex < 0) {
            throw new IllegalArgumentException("Flight ID cannot be null and stopOrderIndex must be non-negative");
        }

        List<StopOver> stopOvers = stopOverRepo.findByFlight_FlightNumber(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with ID: " + flightId));

        StopOver stopOverToDelete = stopOvers.stream()
                .filter(s -> s.getStopOrder() == stopOrderIndex)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("StopOver with stopOrderIndex not found"));

        stopOverRepo.delete(stopOverToDelete);

        // Decrement stopOrder for all subsequent stopovers
        stopOvers.stream()
                .filter(s -> s.getStopOrder() > stopOrderIndex)
                .forEach(s -> {
                    s.setStopOrder(s.getStopOrder() - 1);
                    stopOverRepo.save(s);
                });
    }
}
