package org.jetblue.jetblue.Service.PaymentGetwaysServices;

import com.stripe.exception.StripeException;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Payment;

import java.util.List;

public interface StripeService {

    String processPayment(String paymentId);
    String processPayments(List<String>paymentsIds) throws StripeException;
    String processPayments(String username , Flight flight) throws StripeException;
}
