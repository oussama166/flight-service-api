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

  /**
   * Sends an email to notify the user about the status of their refund request.
   *
   * @param to                the email address of the user or client
   * @param subject           the subject of the email
   * @param user              the User instance containing all information about the user
   * @param bookingPassenger  the BookingPassenger instance containing all information about the booking and the passenger
   * @param flight            the Flight instance associated with the refund request
   * @param seat              the Seat instance associated with the booking
   * @param penalty           the penalty amount applied to the refund
   * @param refundReq         the RefundUserRequest instance containing details of the refund request
   * @param status            the RefundStatus indicating the current status of the refund
   * @throws MessagingException if an error occurs while sending the email
   */
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
