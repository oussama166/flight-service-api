package org.jetblue.jetblue.Service.NotificationServices.Mails;

import com.stripe.model.PaymentIntent;
import jakarta.mail.MessagingException;
import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.jetblue.jetblue.Models.DAO.User;

import java.util.List;

public interface ReceiptMailService {
    void sendReceiptEmail(String to, String subject, User user, List<BookingPassengerPayment> passengerPayments, PaymentIntent paymentIntent) throws MessagingException;
    void sendReceiptEmailWithAttachment(String to, String subject, String body, byte[] attachment, String attachmentName);
    void sendBulkReceiptEmails(List<String> recipients, String subject, String body);
}
