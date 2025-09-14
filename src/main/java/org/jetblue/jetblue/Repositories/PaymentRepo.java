package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentRepo
  extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {}
