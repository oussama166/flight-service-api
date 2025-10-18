package org.jetblue.jetblue.Repositories;

import java.util.Optional;
import org.jetblue.jetblue.Models.DAO.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingStatusRepo extends JpaRepository<BookingStatus, Long> {
  @Query("select b from BookingStatus b where b.status = ?1")
  Optional<BookingStatus> findByStatus(String status);
}
