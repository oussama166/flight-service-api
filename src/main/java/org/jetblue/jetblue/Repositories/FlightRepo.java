package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FlightRepo extends JpaRepository<Flight, Long> {

    @Query(
            "SELECT fl FROM Flight fl WHERE " +
            "(fl.departure.code LIKE %?1% OR fl.departure.name LIKE %?1% OR fl.departure.location LIKE %?1%) " +
            "AND (fl.arrival.code LIKE %?2% OR fl.arrival.name LIKE %?2% OR fl.arrival.location LIKE %?2%)"
    )
    Optional<List<Flight>> findByFlight(String departure, String arrival);

    Optional<Flight> findByFlightNumberOrAirline_AirlineCode(String flightNumber, String airlineCode);

    List<Flight> findByDeparture_CodeAndArrival_CodeAndStatus(String departure_code, String arrival_code, FlightStatus status);

    @Query(
            "SELECT fl FROM Flight  fl where fl.flightNumber LIKE ?1"
    )
    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByDepartureTimeIsAfterAndArrivalTimeIsBeforeAndDeparture_LocationOrArrival_Location(LocalDateTime departureTimeAfter, LocalDateTime arrivalTimeBefore, String departureLocation, String arrivalLocation);
}
