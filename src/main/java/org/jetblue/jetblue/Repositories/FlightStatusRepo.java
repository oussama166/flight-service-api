package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightStatusRepo extends JpaRepository<FlightStatus, Long> {
Optional<FlightStatus> findByStatus(String status);
}
