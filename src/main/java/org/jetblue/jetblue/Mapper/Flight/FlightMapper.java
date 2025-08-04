package org.jetblue.jetblue.Mapper.Flight;

import org.jetblue.jetblue.Mapper.StopOver.StopOverMapper;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.springframework.beans.factory.annotation.Autowired;
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
                .stopOvers(
                        flight.getStopOvers().stream()
                                .map(StopOverMapper::toStopOverResponse)
                                .toList()
                )
                .build();
    }
}
