package org.jetblue.jetblue.Service;

import java.util.List;
import org.jetblue.jetblue.Mapper.Payment.PaymentResponse;

public interface PaymentService {
  List<PaymentResponse> getAllUserPayment();
  List<PaymentResponse> getAllUserPaymentCriteria(
    String status,
    String paymentMethod
  );
}
