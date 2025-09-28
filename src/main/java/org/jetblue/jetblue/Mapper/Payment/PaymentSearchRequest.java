package org.jetblue.jetblue.Mapper.Payment;

public record PaymentSearchRequest(
    String status,
    String paymentMethod
) {
    public PaymentSearchRequest() {
        this(null, null);
    }
}