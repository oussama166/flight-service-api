package org.jetblue.jetblue.Mapper.BookingStatus;

import org.jetblue.jetblue.Models.DAO.BookingStatus;

public class BookingStatusesMapper {

    public static BookingStatus toBookingStatus(BookingStatusRequest bookingStatus) {
        return BookingStatus
                .builder()
                .status(bookingStatus.status())
                .build();
    }
}
