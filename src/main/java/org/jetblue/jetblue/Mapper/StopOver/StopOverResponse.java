package org.jetblue.jetblue.Mapper.StopOver;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StopOverResponse(
    int stopOrder,
    LocalDateTime arrivalTime,
    LocalDateTime departureTime,
    String flightNumber,
    String airportName,
    String airportCode,
    String stopDuration
) {
}
