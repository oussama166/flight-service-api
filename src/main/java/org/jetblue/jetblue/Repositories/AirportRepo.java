package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepo extends JpaRepository<Airport,Long> {


    public Optional<Airport> findByCode( String code);
}
