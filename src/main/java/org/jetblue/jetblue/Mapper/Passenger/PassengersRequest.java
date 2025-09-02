package org.jetblue.jetblue.Mapper.Passenger;

import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;

import java.util.Set;

public record PassengersRequest(
        String flightNumber,
        Set<SeatPassengerDTO> passengers
) {
}
