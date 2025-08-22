package org.jetblue.jetblue.Mapper.Booking;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "User name couldn't be empty !!!")
        String UserName,
        @NotNull(message = "flight number couldn't be empty !!!")
        long FlightNumber,
        @NotNull(message = "seat label couldn't be empty !!!")
        String seatLabel
) {
}
