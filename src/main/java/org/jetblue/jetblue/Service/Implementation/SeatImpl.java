package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Repositories.SeatsRepo;
import org.jetblue.jetblue.Service.SeatService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeatImpl implements SeatService {

    // Injection
    private final SeatsRepo seatsRepo;

    // Implementation

    @Override
    public Seat createSeat(Seat seat) {
        seatsRepo.save(seat);
        return seat;

    }

    @Override
    public Seat updateSeat(int seatId, Seat seatInfo, String flightNumber) {
        // trying to find first the flight
        Seat seat = seatsRepo.findBySeatNumberAndFlight(seatId,flightNumber).orElse(null);

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
        return null;
    }
}
