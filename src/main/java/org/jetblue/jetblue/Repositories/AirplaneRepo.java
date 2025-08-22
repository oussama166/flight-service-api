package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirplaneRepo extends JpaRepository<Airplane,Long> {
    Optional<Airplane> findByName(String airplaneName);
}
