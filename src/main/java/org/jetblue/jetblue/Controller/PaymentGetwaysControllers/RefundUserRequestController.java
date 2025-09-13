package org.jetblue.jetblue.Controller.PaymentGetwaysControllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.jetblue.jetblue.Mapper.RefundUserRequest.RefundUserRequestReq;
import org.jetblue.jetblue.Mapper.RefundUserRequest.RefundUserRequestResponse;
import org.jetblue.jetblue.Service.RefundUserRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments/refund")
@AllArgsConstructor
public class RefundUserRequestController {
  private RefundUserRequestService refundUserRequestService;

  @PostMapping("/requestRefund/")
  @PreAuthorize("hasRole('User')")
  public String requestRefund(
    @RequestBody @Valid RefundUserRequestReq refundUserRequest
  )
    throws MessagingException {
    return refundUserRequestService.processRefund(
      refundUserRequest.paymentId(),
      refundUserRequest.reasonTitle(),
      refundUserRequest.description()
    );
  }

  @GetMapping("/getRefundList/{userName}")
  @PreAuthorize("hasRole('Admin')")
  public ResponseEntity<?> getRefundList(@PathVariable String userName) {
    List<RefundUserRequestResponse> refundPerUser = refundUserRequestService.getRefundPerUser(
      userName
    );

    if (refundPerUser.isEmpty()) {
      return ResponseEntity.ok().body("No refunded data");
    }
    return ResponseEntity.ok().body(refundPerUser);
  }
}
