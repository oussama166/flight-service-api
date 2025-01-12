package org.jetblue.jetblue.Mapper.Flight;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FlightResponse(
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        double price,
        int maxSeat,
        String departure,
        String arrival,
        String airline,
        String flightStatus,
        String flightNumber
) {
}
