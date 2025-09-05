package org.jetblue.jetblue.Controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.RefreshToken;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Repositories.RefreshTokenRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.NotificationServices.Mails.UserMailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController()
@RequestMapping("/api")
@AllArgsConstructor
public class MailVerificationController {
    private final RefreshTokenRepo tokenRepo;
    private final UserRepo userRepo;
    private final UserMailsService userMailsService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) throws MessagingException {
        Optional<RefreshToken> verificationToken = tokenRepo.findByRefreshToken(token);
        if (verificationToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid verification token!");
        }

        User user = verificationToken.get().getUser();
        user.setEnabled(true);
        userRepo.save(user);

        userMailsService.sendWelcomeMail(user.getEmail(), user.getName() + " " + user.getLastName());

        return ResponseEntity.ok().build();
    }
}
