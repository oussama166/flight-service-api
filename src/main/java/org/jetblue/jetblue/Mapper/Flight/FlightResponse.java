package org.jetblue.jetblue.Mapper.Flight;

import lombok.Builder;
import org.jetblue.jetblue.Mapper.StopOver.StopOverResponse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record FlightResponse(
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        String durationFlight,
        double price,
        int maxSeat,
        String departure,
        String arrival,
        String airline,
        String flightStatus,
        String flightNumber,
        List<StopOverResponse> stopOvers
) {
}
