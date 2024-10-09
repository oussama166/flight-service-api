package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Service.FlightService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightImpl implements FlightService {

    private FlightRepo flightRepo;


    @Override
    public Flight setFlight(LocalDateTime departureTime, LocalDateTime arrivalTime, double price, Airport departure, Airport arrival, Airline airline, List<Seat> seats, FlightStatus flightStatus) {
        return null;
    }

    @Override
    public Flight getFlight(String numberFlight) {
        return null;
    }

    @Override
    public Flight updateFlight(String numberFlight, Flight flight) {
        return null;
    }
}
