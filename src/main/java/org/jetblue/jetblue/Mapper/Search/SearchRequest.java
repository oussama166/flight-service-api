package org.jetblue.jetblue.Mapper.Search;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jetblue.jetblue.Models.ENUM.SortBy;

import java.time.LocalDateTime;

public record SearchRequest(
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        String origin,
        String destination,
        String flightStatus
) {
}
