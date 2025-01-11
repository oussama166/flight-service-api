package org.jetblue.jetblue.Mapper.Airline;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AirlineRequest(
        @NotBlank(message = "Airline name could not be blank")
        String airlineName,
        @NotNull
        @NotBlank(message = "Airline code could not be blank")
        String airlineCode,
        String airlineUrl,

        @NotNull
        @DecimalMin(
                value = "1",
                message = "The row formation start from 1 or value greate than 1"
        )
        int rowFormation,
        @NotNull
        @DecimalMin(
                value = "1",
                message = "The column formation start from 1 or value greate than 1"
        )
        int colFormation,
        String airlineLogoLink) {
}
