package org.jetblue.jetblue.Mapper.Booking;

import org.jetblue.jetblue.Models.DAO.Booking;
import org.springframework.stereotype.Service;

@Service
public class BookingMapper {

    public static BookingResponse toBookingResponse(BookingInternal bookingInternal) {
        return BookingResponse.builder()
                .UserName(bookingInternal.UserName())
                .FlightNumber(bookingInternal.FlightNumber())
                .seatLabel(bookingInternal.seatLabel())
                .bookingStatus(bookingInternal.bookingStatus())
                .price(bookingInternal.price())
                .paid(bookingInternal.isPaid())
                .build();
    }

}
