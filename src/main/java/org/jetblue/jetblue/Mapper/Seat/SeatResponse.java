package org.jetblue.jetblue.Mapper.Seat;

import lombok.Builder;
import org.jetblue.jetblue.Models.ENUM.SeatType;

@Builder
public record SeatResponse(
        String airplaneName,
        String seatLabel,
        SeatType seatType,
        boolean isAvailable,
        boolean isReserved,
        boolean isSpecialTrait,
        boolean isLeapEnfantSeat,
        boolean isSold,
        double price
) {
}
