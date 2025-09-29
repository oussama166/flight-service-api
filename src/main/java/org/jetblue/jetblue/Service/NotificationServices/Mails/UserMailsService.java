package org.jetblue.jetblue.Service.NotificationServices.Mails;

import jakarta.mail.MessagingException;
import org.jetblue.jetblue.Models.DAO.User;

public interface UserMailsService {
    void sendUserVerificationMail(User user, String token) throws MessagingException;
    void sendWelcomeMail(String to, String name) throws MessagingException;
    void sendResetPasswordMail(String to, String token);
    void sendBookingConfirmationMail(String to, String bookingDetails);

}
