package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query(value = """
            SELECT fl.id, fl.departure_time, fl.arrival_time, fl.airport_arr, fl.airport_dep,
                   fl.flight_status_id, fl.airline_id
            FROM Flight fl
            LEFT JOIN airport arr ON arr.id = fl.airport_arr
            LEFT JOIN airport dept ON dept.id = fl.airport_dep
            LEFT JOIN flight_status fls ON fls.id = fl.flight_status_id
            LEFT JOIN airline air ON air.id = fl.airline_id
            WHERE fl.departure_time >= :departureStart
              AND fl.arrival_time <= :arrivalEnd
              AND (:depName IS NULL OR :depName = '' OR dept.name = :depName)
              AND (:destName IS NULL OR :destName = '' OR arr.name = :destName)
              AND (:status IS NULL OR :status = '' OR fls.status = :status)
              AND (:airlineName IS NULL OR :airlineName = '' OR air.airline_name = :airlineName)
            """, nativeQuery = true)
    List<Flight> searchFlights(
            @Param("departureStart") LocalDateTime departureStart,
            @Param("arrivalEnd") LocalDateTime arrivalEnd,
            @Param("depName") String depName,
            @Param("destName") String destName,
            @Param("status") String status,
            @Param("airlineName") String airlineName
    );

    List<Flight> findByDepartureTimeIsAfterAndArrivalTimeIsBeforeAndDeparture_Location(LocalDateTime departureStart, LocalDateTime departureEnd,
                                                                 String departureLocation);



}
