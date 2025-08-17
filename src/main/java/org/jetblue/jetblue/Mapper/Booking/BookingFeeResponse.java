package org.jetblue.jetblue.Mapper.Booking;

import lombok.Builder;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Builder
public record BookingFeeResponse(
        String flightNumber,
        List<String> seatLabel,
        double TotalPrice
) {

}
