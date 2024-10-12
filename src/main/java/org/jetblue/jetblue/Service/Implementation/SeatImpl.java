package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.ENUM.SeatType;
import org.jetblue.jetblue.Repositories.AirplaneRepo;
import org.jetblue.jetblue.Repositories.SeatsRepo;
import org.jetblue.jetblue.Service.SeatService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatImpl implements SeatService {

    // Injection
    private final SeatsRepo seatsRepo;
    private final AirplaneRepo airplaneRepo;

    // Implementation


    @Override
    public List<Seat> createSeats(int maxSeatNumber, double price, SeatType seatType, String airplaneName) {
        List<Seat> seats = new ArrayList<Seat>();

        // finding the flight using the flight number
        Airplane airPlane = airplaneRepo.findByName(airplaneName).orElse(null);

        if(airPlane == null) {
            return null;
        }
        // Loop through and create seats up to the max seat number
        for (int i = 1; i <= maxSeatNumber; i++) {
            Seat seat = new Seat(); // Assuming a Seat class with appropriate setters
            seat.setPrice(price); // Assign price
            seat.setSeatType(seatType); // Assign seat type (Economy, Business, etc.)
            seat.setAirPlane(airPlane);

            // Optionally set other properties like availability, etc.
            seat.setAvailable(true); // Assuming there's an available flag for seats

            seats.add(seat); // Add the seat to the list
        }

        seatsRepo.saveAll(seats);
        return seats;
    }

    @Override
    public Seat createSeat(Seat seat) {
        Airplane airplane = airplaneRepo.findByName(seat.getAirPlane().getName()).orElse(null);
        if(airplane == null) {
            return null;
        }
        Seat seatInst = new Seat();
        seatInst.setPrice(seat.getPrice());
        seatInst.setSeatType(seat.getSeatType());
        seatInst.setAirPlane(airplane);
        seatInst.setAvailable(true);
        seatsRepo.save(seatInst);
        return seatInst;

    }

    @Override
    public Seat updateSeat(int seatId, Seat seatInfo, String airplaneName) {
        // trying to find first the flight
        Seat seat = seatsRepo.findBySeatNumberAndAirPlane(seatId,airplaneName).orElse(null);

        if(seat == null) {
            return null;
        }

        seat.setPrice(seatInfo.getPrice());
        seat.setAvailable(seat.isAvailable());
        seat.setSeatType(seatInfo.getSeatType());

        return seatInfo;

    }

    @Override
    public Seat getSeat(int seatId) {
        return seatsRepo.findBySeatNumber(seatId).orElse(null);
    }


}
