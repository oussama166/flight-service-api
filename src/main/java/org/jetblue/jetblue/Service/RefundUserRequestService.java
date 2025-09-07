package org.jetblue.jetblue.Service;

import jakarta.mail.MessagingException;

public interface RefundUserRequestService {
    String processRefund(String paymentId, String reason, String description) throws MessagingException;
}
