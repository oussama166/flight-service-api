package org.jetblue.jetblue.Service;


import org.jetblue.jetblue.Models.DAO.FlightStatus;

public interface FlightStatusService {


    /**
     * Create new Flight status
     *
     * @param flightStatus -- This contains all info about flight
     */
    FlightStatus setFlightStatus(FlightStatus flightStatus);

    /**
     * Getting Flight status by the name
     *
     * @param status -- Status name
     */
    FlightStatus getFlightStatus(String status);

    /**
     * Getting Flight status by the name
     *
     * @param status -- Status name
     * @param flightStatus -- contain all the modified fields
     */
    FlightStatus updateFlightStatus(String status, FlightStatus flightStatus);

    /**
     * Deleting Flight status by the name
     *
     * @param status -- Status name
     */
    Boolean deleteFlightStatus(String status);

}
