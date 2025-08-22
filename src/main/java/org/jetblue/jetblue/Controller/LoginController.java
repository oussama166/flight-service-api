package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.User.LoginRequest;
import org.jetblue.jetblue.Service.Implementation.TokenImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(
)
@AllArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenImpl tokenService; // Inject TokenImpl service


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.userName(), loginRequest.password())
            );
            if (authentication.isAuthenticated()) {
                String token = tokenService.generateToken(authentication);
                return ResponseEntity.ok("token : "+token);
            }
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            // Handle authentication failure
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
