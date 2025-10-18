package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingPassengerRepo
  extends
    JpaRepository<BookingPassenger, Long>,
    JpaSpecificationExecutor<BookingPassenger> {
  boolean existsByPassenger_IdAndSeat_Flight_Id(long passengerId, long seatId);
}
