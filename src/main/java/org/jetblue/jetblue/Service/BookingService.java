package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;

import java.util.List;
import java.util.Set;

public interface BookingService {

    /**
     * Set new booking associate btw user and flight seat
     *
     * @param username -- User information
     * @param seat_number  -- Seat associated with the flight
     * */
    Booking setBooking(String username, long seat_number);


    /**
     * Set new booking associate btw user and List of flight seat
     *
     * @param username -- User information
     * @param passengers  -- List of passenger and seat associated with the flight
     * */
    Booking setBooking(String username , Set<SeatPassengerDTO> passengers);

    /**
     * Get the booking associated with the user account
     *
     * @param user -- user info
     * */
    List<Booking> getUserBookings(String userName);

}
