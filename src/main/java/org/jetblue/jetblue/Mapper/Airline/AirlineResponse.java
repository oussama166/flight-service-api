package org.jetblue.jetblue.Mapper.Airline;

import lombok.Builder;

@Builder
public record AirlineResponse(
        String airlineName,
        String airlineCode,
        String airlineUrl,
        String airlineLogoLink,
        int colFormation,
        int rowFormation) {
}
