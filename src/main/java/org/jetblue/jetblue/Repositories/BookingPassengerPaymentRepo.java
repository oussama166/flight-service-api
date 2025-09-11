package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface BookingPassengerPaymentRepo extends JpaRepository<BookingPassengerPayment, Long>, JpaSpecificationExecutor<BookingPassengerPayment> {
    Optional<BookingPassengerPayment> findByFlight_FlightNumberAndUser_IdAndPassenger_Id(String flightNumber, long userId, Long passengerId);

    List<BookingPassengerPayment> findByFlight_flightNumberAndUser_username(String flightFlightNumber, String userUsername);

    boolean existsByFlight_FlightNumberAndUser_Username(String flightFlightNumber, String userUsername);

    long countByFlight_FlightNumberAndUser_Username(String flightFlightNumber, String userUsername);

    List<BookingPassengerPayment> findByFlight_FlightNumber(String flightNumber);

    List<BookingPassengerPayment> findByUser_UsernameAndFlight_FlightNumber(String username, String flightNumber);

    @Query("""
            select b from BookingPassengerPayment b
            where upper(b.user.username) like upper(?1) and upper(b.flight.flightNumber) like upper(?2)""")
    List<BookingPassengerPayment> findByUser_UsernameLikeIgnoreCaseAndFlight_FlightNumberLikeIgnoreCase(String username, String flightNumber);

}
