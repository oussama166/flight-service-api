package org.jetblue.jetblue.Service.Implementation;

import com.stripe.model.Price;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.jetblue.jetblue.Models.ENUM.ReasonStatus;
import org.jetblue.jetblue.Models.ENUM.RefundStatus;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Repositories.RefundUserRequestRepo;
import org.jetblue.jetblue.Service.NotificationServices.Mails.ReceiptMailService;
import org.jetblue.jetblue.Service.RefundUserRequestService;
import org.jetblue.jetblue.Utils.PriceEngine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.jetblue.jetblue.Utils.PriceEngine.refundPenalty;
import static org.jetblue.jetblue.Utils.UserUtils.getCurrentUsername;

@Service
@AllArgsConstructor
public class RefundUserRequestImpl implements RefundUserRequestService {

    private final PaymentRepo paymentRepo;
    private final RefundUserRequestRepo refundRepo;
    private final ReceiptMailService mailService;


    @Override
    public String processRefund(String paymentId, String reason, String description) throws MessagingException {
        String userName = getCurrentUsername();
        Payment payment = paymentRepo.findById(paymentId).orElse(null);
        List<RefundUserRequest> paymentIntentId = refundRepo.findByPaymentIntentId(payment);

        if (!paymentIntentId.isEmpty()) {
            if (!paymentIntentId.get(0).getStatus().equals(RefundStatus.REJECTED)) {
                return "error: You have already requested a refund for this payment Pending/Approved request exists";
            }
        }
        if (payment == null) {
            return "error: Payment not found";
        }
        if (!payment.getBooking().getUser().getUsername().equals(userName)) {
            return "error: You are not authorized to request a refund for this payment";
        }
        LocalDateTime departure = payment.getBooking().getFlight().getDepartureTime();
        LocalDateTime now = LocalDateTime.now();

        long daysToDeparture = ChronoUnit.DAYS.between(now, departure);


        long daysToDepartureDouble = (long) daysToDeparture;
        double penalty = refundPenalty(daysToDepartureDouble);
        BigDecimal penaltyAmount = new BigDecimal(Double.valueOf(payment.getAmount()) * penalty).setScale(2, RoundingMode.HALF_UP);
        RefundUserRequest refundRequest = RefundUserRequest
                .builder()
                .amount(new BigDecimal(payment.getAmount()))
                .paymentIntentId(payment)
                .currency(payment.getCurrency())
                .reasonTitle(ReasonStatus.fromString(reason))
                .status(RefundStatus.PENDING)
                .reasonDescription(description)
                .refundAmount(penaltyAmount)
                .createdAt(new Date(System.currentTimeMillis()))
                .build();

        refundRepo.save(refundRequest);


        mailService.sendRefundRequestEmail(
                payment.getBooking().getUser().getEmail(),
                "Refund Request Submitted",
                payment.getBooking().getUser(),
                payment.getBooking().getBookingPassengers().get(0),
                payment.getBooking().getFlight(),
                payment.getBooking().getSeat(),
                penalty,
                refundRequest
        );


        return "success: Refund request submitted successfully";

    }

    @Override
    public String getRefundPerUser(String userName){
        List<RefundUserRequest> refunds = refundRepo.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get("paymentIntentId").get("booking").get("user").get("username"), userName
                )
        );
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("Refund Requests for user: ").append(userName).append("\n\n");
        for (RefundUserRequest refund : refunds) {
            sb.append("Refund ID: ").append(refund.getId()).append("\n");
            sb.append("Payment ID: ").append(refund.getPaymentIntentId().getId()).append("\n");
            sb.append("Amount: ").append(refund.getAmount()).append(" ").append(refund.getCurrency()).append("\n");
            sb.append("Refund Amount: ").append(refund.getRefundAmount()).append(" ").append(refund.getCurrency()).append("\n");
            sb.append("Reason: ").append(refund.getReasonTitle()).append("\n");
            sb.append("Description: ").append(refund.getReasonDescription()).append("\n");
            sb.append("Status: ").append(refund.getStatus()).append("\n");
            sb.append("Created At: ").append(sdf.format(refund.getCreatedAt())).append("\n");
            sb.append("-------------------------------\n");
        }
        return sb.toString();

    }

    public List<RefundUserRequest> getAllRefundRequests() {
        return refundRepo.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.like(
                        root.get("status").as(String.class), "%" + "PENDING" + "%")
                );
    }
}
