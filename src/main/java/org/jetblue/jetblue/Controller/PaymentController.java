package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payment", description = "Endpoints to manage user payments")
public class PaymentController {
  private PaymentService paymentService;

  @GetMapping("/getPayment")
  @PreAuthorize("hasRole('USER')")
  @Operation(
    summary = "Get user payments",
    description = "Retrieve all payments for the authenticated user"
  )
  @ApiResponses(
    { @ApiResponse(responseCode = "200", description = "Payments returned") }
  )
  public ResponseEntity<?> getAllPaymentUser() {
    List<PaymentResponse> allUserPayment = paymentService.getAllUserPayment();
    return ResponseEntity.ok().body(allUserPayment);
  }

  @GetMapping("/getPaymentCriteria")
  @PreAuthorize("hasRole('USER')")
  @Operation(
    summary = "Get user payments by criteria",
    description = "Retrieve user payments filtered by status and method"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Filtered payments returned"
      ),
    }
  )
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
