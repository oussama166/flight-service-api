package org.jetblue.jetblue.Controller.PaymentGetwaysControllers;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Service.PaymentGetwaysServices.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/stripe")
@AllArgsConstructor
public class StripeController {
    private StripeService stripeService;

    @PostMapping("/makePayment/{stripeId}")
    public ResponseEntity<?> makePayment(@PathVariable String stripeId) {
        try {
            String processPayment = stripeService.processPayment(stripeId);
            if (processPayment.startsWith("error:")) {
                return ResponseEntity.badRequest().body(processPayment);
            }
            return ResponseEntity.ok(processPayment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
