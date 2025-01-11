package org.jetblue.jetblue.Mapper.Airplane;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AirplaneRequest(
        @NotNull(message = "The name the airplane can not be Empty")
        String name,
        @NotNull(message = "The name the airplane can not be Empty")
        @DecimalMin(value = "10",message = "The system accept only airplane with min seat of 10 seat's")
        int maxSeat,
        String airplaneImageUrl
) {
}
