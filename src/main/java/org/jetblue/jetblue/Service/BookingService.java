package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Booking.BookingFeeResponse;
import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BookingService {

    /**
     * Set new booking associate btw user and flight seat
     *
     * @param username      -- User information
     * @param flight_number -- flight number
     * @param seat_label    -- Seat associated with the flight
     */
    BookingResponse setBooking(String username, long flight_number, String seat_label);


    /**
     * Set new booking associate btw user and List of flight seat
     *
     * @param username   -- User information
     * @param passengers -- List of passenger and seat associated with the flight
     */
    BookingFeeResponse setBookings(String username, Set<SeatPassengerDTO> passengers);

    /**
     * Get the booking associated with the user account
     *
     * @param userName -- user info
     */
    List<BookingResponse> getUserBookings(String userName);

    /**
     * Get all the bookings
     * * @return List of all bookings
     */
    List<Booking> getAllBookings();

    /**
     * Cancel a booking by its ID
     * * @param bookingId the UUID of the booking to cancel
     *
     * @return true if the booking was successfully canceled, false otherwise
     */
    boolean cancelBooking(UUID bookingId);
}
