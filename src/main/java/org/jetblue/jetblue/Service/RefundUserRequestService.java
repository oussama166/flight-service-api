package org.jetblue.jetblue.Service;

import jakarta.mail.MessagingException;
import java.util.List;
import org.jetblue.jetblue.Mapper.RefundUserRequest.RefundUserRequestResponse;

public interface RefundUserRequestService {
  String processRefund(String paymentId, String reason, String description)
    throws MessagingException;

  void validateRefundUser(String paymentId) throws Exception;

  void declineRefundUser(String paymentId) throws Exception;

  List<RefundUserRequestResponse> getRefundPerUser(String userName);

  List<RefundUserRequestResponse> getAllRefundRequests();
}
