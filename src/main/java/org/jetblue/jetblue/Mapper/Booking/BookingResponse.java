package org.jetblue.jetblue.Mapper.Booking;

import lombok.Builder;

@Builder
public record BookingResponse(
        String UserName,
        String FlightNumber,
        String seatLabel,
        String bookingStatus,
        double price,
        boolean paid

) {
}
