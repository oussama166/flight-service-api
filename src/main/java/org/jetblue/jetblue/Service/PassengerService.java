package org.jetblue.jetblue.Service;

import org.jetblue.jetblue.Mapper.Passenger.PassengerRequest;
import org.jetblue.jetblue.Mapper.Passenger.PassengerResponse;
import org.jetblue.jetblue.Models.DAO.Passenger;

public interface PassengerService {
    /**
     * This method is used to create a new passenger.
     *
     * @param passenger The passenger object to be created.
     * @return The created passenger object.
     */
    PassengerResponse createPassenger(PassengerRequest passenger);

    /**
     * This method is used to retrieve a passenger by their ID.
     *
     * @param id The ID of the passenger to be retrieved.
     * @return The passenger object with the specified ID, or null if not found.
     */
    Passenger getPassengerById(Long id);
}
