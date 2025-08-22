package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.status.status <> 'Canceled'")
    List<Booking> findAllBy();
    Optional<List<Booking>> findByUser(User user);

    List<Booking> findBookingsByUser_Username(String username);

    Optional<Booking> findBookingByBookingId(UUID bookingId);

}
