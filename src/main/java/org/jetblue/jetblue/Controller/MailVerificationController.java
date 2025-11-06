package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.RefreshToken;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Repositories.RefreshTokenRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.NotificationServices.Mails.UserMailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(
  name = "MailVerification",
  description = "Endpoints to verify user accounts and send mail notifications"
)
public class MailVerificationController {
  private final RefreshTokenRepo tokenRepo;
  private final UserRepo userRepo;
  private final UserMailsService userMailsService;

  @GetMapping("/verify")
  @Operation(
    summary = "Verify account",
    description = "Verifies a user's account using a token and activates the user"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Account verified and activated"
      ),
      @ApiResponse(responseCode = "400", description = "Invalid token"),
    }
  )
  public ResponseEntity<?> verifyAccount(@RequestParam("token") String token)
    throws MessagingException {
    Optional<RefreshToken> verificationToken = tokenRepo.findByRefreshToken(
      token
    );
    if (verificationToken.isEmpty()) {
      return ResponseEntity.badRequest().body("Invalid verification token!");
    }

    User user = verificationToken.get().getUser();
    user.setEnabled(true);
    userRepo.save(user);

    userMailsService.sendWelcomeMail(
      user.getEmail(),
      user.getName() + " " + user.getLastName()
    );

    return ResponseEntity.ok().build();
  }
}
