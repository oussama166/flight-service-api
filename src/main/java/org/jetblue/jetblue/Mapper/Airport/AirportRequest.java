package org.jetblue.jetblue.Mapper.Airport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AirportRequest(
        @NotBlank(message = "Code can not be null !!!")
        String code,
        @NotNull(message = "Name of airport can not empty !!!")
        String name,
        @NotNull(message = "Location of airport must be insert !!!")
        String location,
        @NotNull(message = "Coordination of airport must be insert !!!")
        double latitude,
        @NotNull(message = "Coordination of airport must be insert !!!")
        double longitude) {
}
