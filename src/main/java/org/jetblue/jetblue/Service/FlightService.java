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
     * @param airplane -- airplane that will take the flight (Airbus A220 ...?)
     * @param maxFirst -- max seats on the first class
     * @param maxSecond -- max seats on the second class
     * @param maxThird -- max seats on the commercial class
     * @param flightStatus -- Status of the flight (canceled ....)
     * */
    Flight setFlight(
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            double price,
            int maxSeat,
            String departure,
            String arrival,
            String airline,
            String airplane,
            int maxFirst,
            int maxSecond,
            int maxThird,
            String flightStatus
    );

    /**
     * Getting flight using the Number Flight
     *
     * @param numberFlight -- each flight has unique flight number
     *
     * */
    Flight getFlight(String numberFlight);

    /**
     * Getting flight using the Number Flight
     *
     * @param numberFlight -- each flight has unique flight number
     * @param airline -- mention the airline of the flight
     *
     * */
    Flight getFlight(String numberFlight, String airline);

    /**
     * Getting flight using the Number Flight
     *
     * @param departure -- departure airport
     * @param arrival -- arrival airport
     * @param flightStatus -- using the flight status to find a flight
     *
     * */
    List<Flight> getFlight(String departure, String arrival, String flightStatus) throws Exception;

    /**
     * Getting List of flights using the departure and the arrival
     *
     * @param departure -- each flight has unique flight number
     * @param arrival -- mention the airline of the flight
     *
     * */
    List<Flight> getFlights(String departure, String arrival);


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
