package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {
}
