package org.jetblue.jetblue.Mapper.Flight;

import org.jetblue.jetblue.Models.DAO.Flight;
import org.springframework.stereotype.Service;

@Service
public class FlightMapper {
    public static FlightResponse toFlightResponse(Flight flight) {
        return FlightResponse
                .builder()
                .flightNumber(flight.getFlightNumber())
                .arrivalTime(flight.getArrivalTime())
                .departureTime(flight.getDepartureTime())
                .price(flight.getPrice())
                .maxSeat(flight.getMaxSeats())
                .departure(flight.getDeparture().getName())
                .arrival(flight.getArrival().getName())
                .airline(flight.getAirline().getAirlineName())
                .flightStatus(flight.getStatus().getStatus())
                .build();
    }
}
