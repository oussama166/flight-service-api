package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.Seat;

public interface SeatService {

    /**
     * Create new seat
     *
     * @param seat -- Adding new seat
     * */

    Seat createSeat(Seat seat);

    /**
     * Update Seat info
     *
     * @param seatId -- seat id
     * @param seatInfo -- seat info
     * */
    Seat updateSeat(int seatId,Seat seatInfo);

    /**
     *
     * Get seat info depend on the set id
     *
     * @param seatId -- seat id
     * */
    Seat getSeat(int seatId);
}
