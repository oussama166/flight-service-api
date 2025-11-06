package org.jetblue.jetblue.Mapper.Search;

import java.time.LocalDateTime;

public record SearchRequest(
                LocalDateTime departureTime,
                LocalDateTime arrivalTime,
                String origin,
                String departureCountry,
                String destination,
                String arrivalCountry,
                String flightStatus) {
}
