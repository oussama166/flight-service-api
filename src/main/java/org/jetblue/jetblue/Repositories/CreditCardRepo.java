package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepo extends JpaRepository<CreditCard, String> {
}
