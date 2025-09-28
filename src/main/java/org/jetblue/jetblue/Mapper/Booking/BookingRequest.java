package org.jetblue.jetblue.Mapper.Booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "User name couldn't be empty !!!")
        @Schema(description = "User name",example = "oussama166")
        String UserName,
        @NotNull(message = "flight number couldn't be empty !!!")
        @Schema(description = "Flight number",example = "5")
        long FlightNumber,
        @NotNull(message = "seat label couldn't be empty !!!")
        @Schema(description = "Seat label associate with flight number",example = "B1")
        String seatLabel
) {
}
