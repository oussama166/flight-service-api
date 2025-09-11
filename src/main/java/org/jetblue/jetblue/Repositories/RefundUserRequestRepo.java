package org.jetblue.jetblue.Repositories;

import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RefundUserRequestRepo extends JpaRepository<RefundUserRequest, Long> , JpaSpecificationExecutor<RefundUserRequest> {
    List<RefundUserRequest> findByPaymentIntentId(Payment paymentIntentId);
}
