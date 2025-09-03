package org.jetblue.jetblue.Service.MailingServices;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailImpl {
    @Value("${server.port}")
    private final String port;
    private final JavaMailSender mailSender;

    public EmailImpl(JavaMailSender mailSender, @Value("${server.port}") String port) {
        this.mailSender = mailSender;
        this.port = port;
    }

    public void sendVerificationEmail(User user, String token) throws MessagingException {
        String verificationLink = String.format("http://localhost:%s/api/verify?token=%s", port, token);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String htmlContent = "<!DOCTYPE html>" +
                             "<html lang='en'>" +
                             "<head>" +
                             "<meta charset='UTF-8'>" +
                             "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                             "<style>" +
                             "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                             "  .container { max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
                             "  h2 { color: #333333; }" +
                             "  p { font-size: 16px; color: #555555; }" +
                             "  .btn { display: inline-block; padding: 12px 20px; margin-top: 20px; font-size: 16px; color: #ffffff; background-color: #007bff; text-decoration: none; border-radius: 5px; }" +
                             "  .footer { margin-top: 30px; font-size: 12px; color: #999999; text-align: center; }" +
                             "</style>" +
                             "</head>" +
                             "<body>" +
                             "  <div class='container'>" +
                             "    <h2>Hello " + user.getUsername() + "!</h2>" +
                             "    <p>Thank you for registering. Please verify your email by clicking the button below:</p>" +
                             "    <a href='" + verificationLink + "' class='btn'>Verify Email</a>" +
                             "    <p class='footer'>If you didn't create an account, please ignore this email.</p>" +
                             "  </div>" +
                             "</body>" +
                             "</html>";

        helper.setTo(user.getEmail());
        helper.setSubject("Verify Your Email");
        helper.setText(
                htmlContent,
                true // true indicates HTML
        );

        mailSender.send(message);
    }


}
