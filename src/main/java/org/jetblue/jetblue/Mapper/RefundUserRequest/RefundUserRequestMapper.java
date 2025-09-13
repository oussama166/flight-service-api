package org.jetblue.jetblue.Mapper.RefundUserRequest;

import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.springframework.stereotype.Service;

@Service
public class RefundUserRequestMapper {

  public static RefundUserRequestResponse toRefundUserRequestResponse(
    RefundUserRequest refundUserRequest
  ) {
    return RefundUserRequestResponse
      .builder()
      .refundId(refundUserRequest.getId())
      .paymentId(refundUserRequest.getPaymentIntentId().getId())
      .Amount(
        String.format(
          "%s %s",
          refundUserRequest.getAmount().toString(),
          refundUserRequest.getCurrency()
        )
      )
      .RefundedAmount(
        String.format(
          "%s %s",
          refundUserRequest.getRefundAmount(),
          refundUserRequest.getCurrency()
        )
      )
      .Reason(refundUserRequest.getReasonTitle().name())
      .Description(refundUserRequest.getReasonDescription())
      .CreateAt(refundUserRequest.getCreatedAt().toString())
      .build();
  }
}
