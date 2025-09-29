package org.jetblue.jetblue.Service.Implementation.NotificationImpl.Mails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Service.NotificationServices.Mails.UserMailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static org.jetblue.jetblue.Utils.MailTemplatesUtils.generateVerificationEmail;
import static org.jetblue.jetblue.Utils.MailTemplatesUtils.generateWelcomeEmailBody;

@Service
public class UserMailImpl implements UserMailsService {


    @Value("${server.port}")
    private final String port;
    private final JavaMailSender mailSender;


    public UserMailImpl(JavaMailSender mailSender, @Value("${server.port}") String port) {
        this.mailSender = mailSender;
        this.port = port;
    }

    @Override
    public void sendUserVerificationMail(User user, String token) throws MessagingException {
        String verificationLink = String.format("http://localhost:%s/api/verify?token=%s", port, token);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(user.getEmail());
        helper.setSubject("Verify Your Email");
        helper.setText(
                generateVerificationEmail(user.getName() + " " + user.getLastName(), verificationLink),
                true
        );

        mailSender.send(message);
    }

    @Override
    public void sendWelcomeMail(String to, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Welcome to JetBlue!");
        helper.setText(
                generateWelcomeEmailBody(name),
                true
        );
        mailSender.send(message);


    }

    @Override
    public void sendResetPasswordMail(String to, String token) {

    }

    @Override
    public void sendBookingConfirmationMail(String to, String bookingDetails) {

    }
}