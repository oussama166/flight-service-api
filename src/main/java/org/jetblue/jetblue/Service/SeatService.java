package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Seat.SeatResponse;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.DTO.SeatCreationRequest;
import org.jetblue.jetblue.Models.ENUM.SeatType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SeatService {

    /**
     * Create new seat
     *
     * @param seat -- Adding new seat
     */

    SeatResponse createSeat(SeatCreate seat);

    /**
     * Create range of seats
     *
     * @param price        -- Price of the seat
     * @param seatType     -- Seat type [FIRST_CLASS,SECOND_CLASS,ECONOMY_CLASS]
     * @param flightNumber --Number of the flight
     * @param startCol -- Create seat from start seat
     */
    List<SeatResponse> createSeats(SeatCreationRequest seatCreationRequest);


    /**
     * Update Seat info
     *
     * @param seatLabel   -- seat id
     * @param seatInfo -- seat info
     */
    Seat updateSeat(String seatLabel, Seat seatInfo, String flightNumber) throws ExecutionException;

    /**
     * Get seat info depend on the set id
     *
     * @param seatLabel -- seat id
     */
    SeatResponse getSeat(String seatLabel);

    /**
     * Getting the all seat associated with flight
     * @param flightNumber
     *  */
    List<SeatResponse> getAllSeats(String flightNumber) ;
}
