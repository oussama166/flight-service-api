package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findPassengerByUser_UsernameAndFirstNameAndLastNameAndBirthDate(String userUsername, String firstName, String lastName, LocalDate birthDate);
}
