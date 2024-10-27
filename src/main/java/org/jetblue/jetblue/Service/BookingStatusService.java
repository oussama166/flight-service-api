package org.jetblue.jetblue.Service;


import org.jetblue.jetblue.Models.DAO.BookingStatus;

import java.util.List;

public interface BookingStatusService {

    /**
    * This function is dedicated to adding new Booking status
    *
    * @param bookingStatus -- given a hole booking status info object
    * */
    BookingStatus setBookingStatus(BookingStatus bookingStatus);

    /**
     * This function is dedicated to adding a list of booking status to dataset
     *
     * @param bookingStatuses -- Booking status list
     * */
    BookingStatus setBookingStatus(List<BookingStatus> bookingStatuses);

    /**
     * This function getting a booking status by the name
     *
     * @param bookingName -- search by the name
     * */

    BookingStatus getBookingStatus(String bookingName);

    List<BookingStatus> getAllBookingStatuses();

    BookingStatus updateBookingStatus(BookingStatus bookingStatus);

    BookingStatus deleteBookingStatus(String bookingId);
}
