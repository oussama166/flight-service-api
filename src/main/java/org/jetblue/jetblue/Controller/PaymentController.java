package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Payment.PaymentResponse;
import org.jetblue.jetblue.Mapper.Payment.PaymentSearchRequest;
import org.jetblue.jetblue.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaymentController {
  private PaymentService paymentService;

  @GetMapping("/getPayment")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getAllPaymentUser() {
    List<PaymentResponse> allUserPayment = paymentService.getAllUserPayment();
    return ResponseEntity.ok().body(allUserPayment);
  }

  @GetMapping("/getPaymentCriteria")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getAllPaymentUserCriteria(
    @RequestBody PaymentSearchRequest paymentSearchRequest
  ) {
    List<PaymentResponse> allUserPayment = paymentService.getAllUserPaymentCriteria(
      paymentSearchRequest.status(),
      paymentSearchRequest.paymentMethod()
    );
    return ResponseEntity.ok().body(allUserPayment);
  }
}
