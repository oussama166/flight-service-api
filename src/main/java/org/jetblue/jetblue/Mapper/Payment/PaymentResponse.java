package org.jetblue.jetblue.Mapper.Payment;

import lombok.Builder;

@Builder
public record PaymentResponse(
    String amount,
    String paymentMethod,
    String paymentStatus,
    String paymentGetway,
    String transactionId,
    String orderId,
    String creditCardLastNumber
) {
}