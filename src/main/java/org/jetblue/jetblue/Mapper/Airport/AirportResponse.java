package org.jetblue.jetblue.Mapper.Airport;

import lombok.Builder;

@Builder
public record AirportResponse(
        String code,
        String location,
        String name,
        double longitude,
        double latitude
) {
}
