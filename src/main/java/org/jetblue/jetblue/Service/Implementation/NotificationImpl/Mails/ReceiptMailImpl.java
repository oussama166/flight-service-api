package org.jetblue.jetblue.Service.Implementation.NotificationImpl.Mails;

import static org.jetblue.jetblue.Utils.MailTemplatesUtils.generateReceiptEmail;
import static org.jetblue.jetblue.Utils.MailTemplatesUtils.generateRefundRequestBody;

import com.stripe.model.PaymentIntent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Models.ENUM.RefundStatus;
import org.jetblue.jetblue.Service.NotificationServices.Mails.ReceiptMailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ReceiptMailImpl implements ReceiptMailService {
  @Value("${server.port}")
  private final String port;

  private final JavaMailSender mailSender;

  public ReceiptMailImpl(
    JavaMailSender mailSender,
    @Value("${server.port}") String port
  ) {
    this.mailSender = mailSender;
    this.port = port;
  }

  @Override
  public void sendReceiptEmail(
    String to,
    String subject,
    User user,
    List<BookingPassengerPayment> passengerPayments,
    PaymentIntent paymentIntent
  )
    throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(
      generateReceiptEmail(user, passengerPayments, paymentIntent),
      true
    );

    mailSender.send(mimeMessage);
  }

  @Override
  public void sendReceiptEmailWithAttachment(
    String to,
    String subject,
    String body,
    byte[] attachment,
    String attachmentName
  ) {}

  @Override
  public void sendBulkReceiptEmails(
    List<String> recipients,
    String subject,
    String body
  ) {}

  @Override
  public void sendRefundRequestEmail(
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
    throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(
      generateRefundRequestBody(
        user,
        flight,
        seat,
        bookingPassenger,
        penalty,
        refundReq,
        status
      ),
      true
    );

    mailSender.send(mimeMessage);
  }
}
