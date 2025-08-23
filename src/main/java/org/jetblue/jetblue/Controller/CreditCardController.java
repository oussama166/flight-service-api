package org.jetblue.jetblue.Controller;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardRequest;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardResponse;
import org.jetblue.jetblue.Mapper.CreditCard.FullCreditCardInfoResponse;
import org.jetblue.jetblue.Service.CreditCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(value = "/api")
@AllArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping(value = "/credit-card/{username}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createCreditCard(
            @PathVariable String username,
            @RequestBody CreditCardRequest creditCard) {
        try {
            CreditCardResponse createdCard = creditCardService.addCreditCardToUser(username, creditCard);
            return ResponseEntity.status(201).body(createdCard);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
        }
    }
    @GetMapping(
            value = "/credit-card/{username}",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> getCreditCardByUsername(@PathVariable String username) {
        try {
            List<FullCreditCardInfoResponse> creditCard = creditCardService.CreditCardByUsername(username);
            if (creditCard == null) {
                return ResponseEntity.status(404).body("Credit card not found for user: " + username);
            }
            return ResponseEntity.ok(creditCard);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
        }
    }

    @DeleteMapping(
            value = "/credit-card/{username}/{lastFourDigits}",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> deleteCreditCardByUsername(@PathVariable String username, @PathVariable String lastFourDigits) {
        try {
            boolean deleted = creditCardService.DeleteCreditCardByUsername(username, lastFourDigits);
            if (deleted) {
                return ResponseEntity.ok("Credit card deleted successfully for user: " + username);
            } else {
                return ResponseEntity.status(404).body("Credit card not found for user: " + username);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
        }
    }
}
