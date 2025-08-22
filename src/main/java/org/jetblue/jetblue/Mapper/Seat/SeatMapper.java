package org.jetblue.jetblue.Mapper.Seat;

import org.jetblue.jetblue.Models.DAO.Seat;
import org.springframework.stereotype.Service;

@Service
public class SeatMapper {
    public static SeatResponse toSeatResponse(Seat seat){
        return SeatResponse.builder()
                .airplaneName(seat.getAirplane().getName())
                .seatType(seat.getSeatType())
                .seatLabel(seat.getSeatLabel())
                .isAvailable(seat.isAvailable())
                .isSpecialTrait(seat.isSpecialTrait())
                .isLeapEnfantSeat(seat.isLeapEnfantSeat())
                .isSold(seat.isSold())
                .price(seat.getPrice())
                .build();
    }
}
