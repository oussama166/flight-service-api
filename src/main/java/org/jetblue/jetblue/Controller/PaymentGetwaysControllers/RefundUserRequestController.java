package org.jetblue.jetblue.Controller.PaymentGetwaysControllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.RefundUserRequest.RefundUserRequestReq;
import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.jetblue.jetblue.Service.RefundUserRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/refund")
@AllArgsConstructor
public class RefundUserRequestController {

    private RefundUserRequestService refundUserRequestService;

    @PostMapping("/requestRefund/")
    @PreAuthorize("hasRole('User')")
    public String requestRefund(@RequestBody @Valid RefundUserRequestReq refundUserRequest) throws MessagingException {
        return refundUserRequestService.processRefund(
                refundUserRequest.paymentId(),
                refundUserRequest.reasonTitle(),
                refundUserRequest.description()
        );
    }
    @GetMapping("/getRefundList/{userName}")
    @PreAuthorize("hasRole('Admin')")
    public String getRefundList(
            @PathVariable String userName
    ) {
        return refundUserRequestService.getRefundPerUser(userName);
    }


}
