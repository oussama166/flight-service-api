package org.jetblue.jetblue.Mapper.Flight.FlightSearch;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record FlightSearchRequest(
        @NotNull(message = "Departure required to make the search!!!")
        String departure,
        @NotNull(message = "Arrival required to make the search!!!")
        String arrival,
        @DefaultValue(value = "On Time")
        String statusFlight
) {
}
