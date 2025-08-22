package org.jetblue.jetblue.Mapper.Airplane;

import lombok.Builder;

@Builder
public record AirplaneResponse(
        String name,
        int maxSeat,
        String airplaneImageUrl
) {
}
