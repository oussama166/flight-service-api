package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Airport.AirportRequest;
import org.jetblue.jetblue.Mapper.Airport.AirportResponse;
import org.jetblue.jetblue.Models.DAO.Airport;

import java.util.List;

public interface AirportService {

    /**
     * This function is to add new airport to database
     *
     * @return airport
     */

    Airport createAirport(Airport airport);
    /**
     * This function is to add list of new airports to database
     *
     * @return airports
     * */
    List<Airport> createAirports(List<AirportRequest> airports);

    /**
     * This function update the credential info about airport
     *
     * @return Airport
     */
    Airport updateAirport(String code, Airport airport);

    /**
     * This function is for getting airport info using the code of airport
     *
     * @return Airport
     */
    Airport getAirport(String code);

    /**
     * This function is dedicated to get all the airports
     *
     * @return List<Airports>
     */
    List<AirportResponse> getAllAirports();


    /**
     * This function is for deleting airports from the database using the code of the airport
     *
     * @retun Boolean
     */
    boolean deleteAirport(String code);

}
