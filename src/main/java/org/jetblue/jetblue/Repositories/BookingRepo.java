package org.jetblue.jetblue.Repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepo extends JpaRepository<Booking, Long> {
  @Query("SELECT b FROM Booking b WHERE b.status.status <> 'Cancelled'")
  List<Booking> findAllBy();

  Optional<List<Booking>> findByUser(User user);

  List<Booking> findBookingsByUser_Username(String username);

  Optional<Booking> findBookingByBookingId(UUID bookingId);

  Optional<Booking> findByBookingId(UUID bookingId);
}
