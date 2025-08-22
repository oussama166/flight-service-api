package org.jetblue.jetblue.Mapper.Booking;

public record BookingInternal(
        String UserName,
        String FlightNumber,
        String seatLabel,
        String bookingStatus,
        double price,
        boolean isPaid
) {
}
