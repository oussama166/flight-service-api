package org.jetblue.jetblue.Mapper.Payment;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentMapper {

  public static PaymentResponse toPaymentResponse(Payment payment) {
    return PaymentResponse
      .builder()
      .amount(payment.getAmount() + " " + payment.getCurrency())
      .creditCardLastNumber(
        "**** **** **** " + payment.getCreditCard().getLastFourDigits()
      )
      .orderId(payment.getOrderId())
      .paymentGetway(payment.getPaymentGateway().name())
      .paymentMethod(payment.getMethod().name())
      .paymentStatus(payment.getStatus().name())
      .transactionId(payment.getTransactionId())
      .orderId(payment.getId())
      .build();
  }
}
