package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
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
        return null;
    }

    @Override
    public Seat updateSeat(int seatId, Seat seatInfo) {
        return null;
    }

    @Override
    public Seat getSeat(int seatId) {
        return null;
    }
}
