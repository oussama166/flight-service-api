package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatsRepo extends JpaRepository<Seat, Long> {
}
