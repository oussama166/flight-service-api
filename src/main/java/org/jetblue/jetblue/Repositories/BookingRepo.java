package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    Optional<List<Booking>> findByUser(User user);

    List<Booking> findBookingsByUser_Username(String username);
}
