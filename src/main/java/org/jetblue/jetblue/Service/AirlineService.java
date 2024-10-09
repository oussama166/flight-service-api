package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Models.DAO.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface AirlineService {

    /**
     * This function dedicated to register new Airline
     *
     * @return Airline
     *  */
    Airline setAirline(Airline airline);


    /**
     * This function to get all airline by name
     *
     * @return Airline
     * */
    Airline getAirline(String airlineName);

    /**
     *
     * This function is to update the Airline Info
     *
     * @param airlineName - the given name of the airline
     * @param airline - airline info that need to be updated
     *
     * @return Airline - return airline object with all the updated field
     * */
    Airline updateAirline(String airlineName,Airline airline);

    /**
     *
     * This function allow to delete airline from the database
     *
     * @param airlineName - The given name of the airline
     *
     * @return boolean
     * */
    Boolean deleteAirline(String airlineName);

    /**
     * This function is used to retrieve airline flight information based on the provided parameters.
     *
     *
     * @param from - The departure location (city or airport code).
     * @param to - The destination location (city or airport code).
     * @param airlineName - The name of the airline operating the flight.
     *
     */

    Set<Flight> getAllAirlineFlights(LocalDateTime from, LocalDateTime to, String airlineName);

    /**
     * This function is to get all the airlines in the database
     *
     * @return List
     * */
    List<Airline> getAllAirlines();



}
