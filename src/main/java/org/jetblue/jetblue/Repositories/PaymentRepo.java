package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, String> {
}
