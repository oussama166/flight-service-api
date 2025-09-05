package org.jetblue.jetblue.Service.PaymentGetwaysServices;

import com.stripe.exception.StripeException;
import jakarta.mail.MessagingException;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Payment;

import java.util.List;

public interface StripeService {

    String processPayment(String paymentId);
    String processPayments(String flightId) throws StripeException, MessagingException;
    String processPayments(String username , Flight flight) throws StripeException;
}
