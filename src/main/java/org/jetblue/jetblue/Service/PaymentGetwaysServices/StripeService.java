package org.jetblue.jetblue.Service.PaymentGetwaysServices;

import org.jetblue.jetblue.Models.DAO.Payment;

public interface StripeService {

    String processPayment(String paymentId);
}
