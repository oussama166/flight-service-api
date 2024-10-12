package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepo extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);
}
