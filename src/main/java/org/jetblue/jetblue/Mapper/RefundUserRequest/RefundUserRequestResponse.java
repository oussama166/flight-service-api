package org.jetblue.jetblue.Mapper.RefundUserRequest;

import lombok.Builder;

@Builder
public record RefundUserRequestResponse(
    String refundId,
    String paymentId,
    String Amount,
    String RefundedAmount,
    String Reason,
    String Description,
    String Status,
    String CreateAt
) {
}