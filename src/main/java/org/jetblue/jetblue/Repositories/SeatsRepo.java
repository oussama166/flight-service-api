package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatsRepo extends JpaRepository<Seat, Long> {

    @Query(
            "SELECT st FROM Seat st where st.seatLabel = ?1 AND st.flight.flightNumber LIKE %?2% "
    )
    Optional<Seat> findBySeatNumberAndFlight(String seatLabel, String airplaneName);

    @Query(
            value = "SELECT fl FROM Flight fl WHERE fl.flightNumber = ?1"
    )
    Optional<Flight> findByFlightFlightNumber(String flightNumber);


    boolean existsByFlightFlightNumberAndSeatLabel(String flightNumber, String seatLabel);


    Optional<Flight> findFlightBySeatLabel(String seatLabel);



    @Query("select s from Seat s where s.flight.id = ?1 and s.seatLabel = ?2")
    Optional<Seat> findByFlight_IdAndSeatLabel(long id, String seatLabel);


    Optional<Seat> findSeatByFlag(String flag);

    @Query(
            "SELECT s FROM Seat s WHERE s.flight.flightNumber = ?1 ORDER BY s.seatLabel asc"
    )
    List<Seat> findByFlight_FlightNumber(String flightNumber);

    Optional<Seat> findFirstByFlight_IdAndSeatLabel(long id, String seatLabel);


}
