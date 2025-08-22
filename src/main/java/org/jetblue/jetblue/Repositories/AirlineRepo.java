package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Airline;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface AirlineRepo extends JpaRepository<Airline, Long> {
    Optional<Airline> findByAirlineName(String airlineName);

    Optional<Airline> findByAirlineCode(String code);

    @Query(
            value = "SELECT fl FROM Flight fl INNER JOIN fl.airline al WHERE fl.departureTime = ?1 AND fl.arrivalTime = ?2 AND al.airlineName LIKE %?3%"
    )
    Set<Flight> findByDepartureTimeAndArrivalTimeAndAirlineName(LocalDateTime departureTime, LocalDateTime arrivalTime, String airlineName);
}
