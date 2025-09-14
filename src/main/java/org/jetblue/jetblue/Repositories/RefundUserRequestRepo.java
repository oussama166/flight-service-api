package org.jetblue.jetblue.Repositories;

import java.util.List;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RefundUserRequestRepo
  extends
    JpaRepository<RefundUserRequest, String>,
    JpaSpecificationExecutor<RefundUserRequest> {
  List<RefundUserRequest> findByPaymentIntentId(Payment paymentIntentId);
}
