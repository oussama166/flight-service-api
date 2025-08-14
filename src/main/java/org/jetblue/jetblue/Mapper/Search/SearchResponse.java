package org.jetblue.jetblue.Mapper.Search;

import lombok.Builder;

@Builder
public record SearchResponse(
        String departure,
        String arrival,
        String departureDate,
        String returnDate
) {
}
