package org.jetblue.jetblue.Repositories;

import java.util.Optional;
import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightStatusRepo extends JpaRepository<FlightStatus, Long> {
  Optional<FlightStatus> findByStatus(String status);
}
