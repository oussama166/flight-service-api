package org.jetblue.jetblue.Service;


import org.jetblue.jetblue.Mapper.BookingStatus.BookingStatusRequest;
import org.jetblue.jetblue.Models.DAO.BookingStatus;

import java.util.List;

public interface BookingStatusService {

    /**
    * This function is dedicated to adding new BookingRepo status
    *
    * @param bookingStatus -- given a hole booking status info object
    * */
    BookingStatus setBookingStatus(BookingStatusRequest bookingStatus);

    /**
     * This function is dedicated to adding a list of booking status to dataset
     *
     * @param bookingStatuses -- BookingRepo status list
     * */
    BookingStatus setBookingStatus(List<BookingStatusRequest> bookingStatuses);

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
