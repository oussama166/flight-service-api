package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.ENUM.SeatType;

import java.util.List;

public interface SeatService {

    /**
     * Create new seat
     *
     * @param seat -- Adding new seat
     * */

    Seat createSeat(SeatCreate seat);

    /**
     * Create range of seats
     *
     * @param price -- Price of the seat
     * @param seatType -- Seat type [FIRST_CLASS,SECOND_CLASS,ECONOMY_CLASS]
     * @param flightNumber --Number of the flight
     *
     */
    List<Seat> createSeats(int maxSeatNumber, double price, SeatType seatType, String flightNumber);



    /**
     * Update Seat info
     *
     * @param seatId -- seat id
     * @param seatInfo -- seat info
     * */
    Seat updateSeat(int seatId, Seat seatInfo, String flightNumber);

    /**
     *
     * Get seat info depend on the set id
     *
     * @param seatId -- seat id
     * */
    Seat getSeat(int seatId);
}
