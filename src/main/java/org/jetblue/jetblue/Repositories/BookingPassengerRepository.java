package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, UUID> {
}