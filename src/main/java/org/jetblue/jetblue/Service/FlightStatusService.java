package org.jetblue.jetblue.Service;


import org.jetblue.jetblue.Mapper.FlightStatus.FlightStatusResponse;
import org.jetblue.jetblue.Models.DAO.FlightStatus;

import java.util.List;

public interface FlightStatusService {


    /**
     * Create new Flight status
     *
     * @param flightStatus -- This contains all info about flight
     */
    FlightStatus setFlightStatus(FlightStatus flightStatus);

    /**
     * Create new flights status
     *
     * @param status -- List of all the status that we need to be added
     * */

    boolean setListFlightStatus(List<String> status);
    /**
     * Getting Flight status by the name
     *
     * @param status -- Status name
     */
    FlightStatus getFlightStatus(String status);

    /**
     *
     * Getting all the flight status on the database
     * */
    List<FlightStatusResponse> getAllFlightStatus();

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
