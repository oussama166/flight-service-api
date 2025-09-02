package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingPassengerRepo extends JpaRepository<BookingPassenger, Long> {

    boolean existsByPassenger_IdAndSeat_Flight_Id(long passengerId, long seatId);

}