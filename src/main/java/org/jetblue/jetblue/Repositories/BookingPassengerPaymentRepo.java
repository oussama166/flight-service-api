package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface BookingPassengerPaymentRepo extends JpaRepository<BookingPassengerPayment, Long> {
    Optional<BookingPassengerPayment> findByFlight_FlightNumberAndUser_IdAndPassenger_Id(String flightNumber, long userId, Long passengerId);

    List<BookingPassengerPayment> findByFlight_flightNumberAndUser_username(String flightFlightNumber, String userUsername);

    boolean existsByFlight_FlightNumberAndUser_Username(String flightFlightNumber, String userUsername);

    long countByFlight_FlightNumberAndUser_Username(String flightFlightNumber, String userUsername);
}
