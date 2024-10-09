package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.*;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {

    /**
     * Create flight using the name of two points
     *
     * @param departureTime -- Date and time when the flight start
     * @param arrivalTime -- Date and time when the flight end
     * @param price -- Price of the flight should be double
     * @param departure -- Departure airport
     * @param arrival -- Arrival airport
     * @param airline -- airline of the flight (air maroc ....?)
     * @param seats -- List of all the seat number in the flight
     * @param flightStatus -- Status of the flight (canceled ....)
     * */
    Flight setFlight(
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            double price,
            Airport departure,
            Airport arrival,
            Airline airline,
            List<Seat> seats,
            FlightStatus flightStatus
    );

    /**
     * Getting flight using the Number Flight
     *
     * @param numberFlight -- each flight has unique flight number
     *
     * */
    Flight getFlight(String numberFlight);

    /**
     * Update flight info using the Number Flight
     *
     * @param numberFlight -- each flight has unique flight number
     * @param flight       -- giving flight info that need to be updated
     *
     * */
    Flight updateFlight(
            String numberFlight,
            Flight flight
    );



}
