package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepo extends JpaRepository<CreditCard, String> {
    Optional<CreditCard> findByUser_Username(String userUsername);
}
