package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingStatusRepo extends JpaRepository<BookingStatus,Long> {
    Optional<BookingStatus> findByStatus(String status);
}
