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

import static org.jetblue.jetblue.Utils.MailTemplatesUtils.generateVerificationEmail;

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

    }


}
