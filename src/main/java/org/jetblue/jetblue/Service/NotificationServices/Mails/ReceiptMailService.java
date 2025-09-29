package org.jetblue.jetblue.Service.NotificationServices.Mails;

import com.stripe.model.PaymentIntent;
import jakarta.mail.MessagingException;
import java.util.List;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Models.ENUM.RefundStatus;

public interface ReceiptMailService {
  void sendReceiptEmail(
    String to,
    String subject,
    User user,
    List<BookingPassengerPayment> passengerPayments,
    PaymentIntent paymentIntent
  )
    throws MessagingException;

  void sendReceiptEmailWithAttachment(
    String to,
    String subject,
    String body,
    byte[] attachment,
    String attachmentName
  );

  void sendBulkReceiptEmails(
    List<String> recipients,
    String subject,
    String body
  );

  void sendRefundRequestEmail(
    String to,
    String subject,
    User user,
    BookingPassenger bookingPassenger,
    Flight flight,
    Seat seat,
    double penalty,
    RefundUserRequest refundReq,
    RefundStatus status
  )
    throws MessagingException;
}
