package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.StopOver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopOverRepo extends JpaRepository<StopOver,Long> {

    Optional<List<StopOver>> findByFlight_FlightNumber(String flightFlightNumber);
}
