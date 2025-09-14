package org.jetblue.jetblue.Service.Implementation;

import static org.jetblue.jetblue.Utils.PriceEngine.refundPenalty;
import static org.jetblue.jetblue.Utils.UserUtils.getCurrentUsername;

import jakarta.mail.MessagingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Mapper.RefundUserRequest.RefundUserRequestMapper;
import org.jetblue.jetblue.Mapper.RefundUserRequest.RefundUserRequestResponse;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.DAO.RefundUserRequest;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.ENUM.ReasonStatus;
import org.jetblue.jetblue.Models.ENUM.RefundStatus;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Repositories.RefundUserRequestRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.NotificationServices.Mails.ReceiptMailService;
import org.jetblue.jetblue.Service.PaymentGetwaysServices.StripeService;
import org.jetblue.jetblue.Service.RefundUserRequestService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RefundUserRequestImpl implements RefundUserRequestService {
  private final PaymentRepo paymentRepo;
  private final RefundUserRequestRepo refundRepo;
  private final ReceiptMailService mailService;
  private final UserRepo userRepo;

  private final StripeService stripeService;

  @Override
  public String processRefund(
    String paymentId,
    String reason,
    String description
  )
    throws MessagingException {
    String userName = getCurrentUsername();
    Payment payment = paymentRepo.findById(paymentId).orElse(null);
    List<RefundUserRequest> paymentIntentId = refundRepo.findByPaymentIntentId(
      payment
    );

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
    LocalDateTime departure = payment
      .getBooking()
      .getFlight()
      .getDepartureTime();
    LocalDateTime now = LocalDateTime.now();

    long daysToDeparture = ChronoUnit.DAYS.between(now, departure);

    long daysToDepartureDouble = (long) daysToDeparture;
    double penalty = refundPenalty(daysToDepartureDouble);
    BigDecimal penaltyAmount = new BigDecimal(
      Double.valueOf(payment.getAmount()) * penalty
    )
    .setScale(2, RoundingMode.HALF_UP);
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
      refundRequest,
      payment.getRefundUserRequest().getStatus()
    );

    return "success: Refund request submitted successfully";
  }

  @Override
  public List<RefundUserRequestResponse> getRefundPerUser(String userName) {
    // rebuild this one to be able to create list of user request refund
    List<RefundUserRequest> refunds = refundRepo.findAll(
      (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(
          root
            .get("paymentIntentId")
            .get("booking")
            .get("user")
            .get("username"),
          userName
        )
    );

    List<RefundUserRequestResponse> listORefundUserRequestResponses = refunds
      .stream()
      .map(RefundUserRequestMapper::toRefundUserRequestResponse)
      .toList();

    return listORefundUserRequestResponses;
  }

  @Override
  public List<RefundUserRequestResponse> getAllRefundRequests() {
    return refundRepo
      .findAll(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.like(
            root.get("status").as(String.class),
            "%" + "PENDING" + "%"
          ),
        Sort.by(Sort.Direction.ASC, "status")
      )
      .stream()
      .map(RefundUserRequestMapper::toRefundUserRequestResponse)
      .toList();
  }

  @Override
  public void declineRefundUser(String paymentId) throws Exception {
    String handelBy = getCurrentUsername();

    User treatBy = userRepo
      .findByUsername(handelBy)
      .orElseThrow(
        () -> new Exception("Admin not found to treat the refund request !!!")
      );

    Optional<RefundUserRequest> refundRequest = refundRepo.findOne(
      (root, query, builder) -> builder.equal(root.get("id"), paymentId)
    );

    if (!refundRequest.isPresent()) {
      throw new Exception(
        String.format("The request with the id  %s not founded!", paymentId)
      );
    }
    // we will close the ticket or the request by setting the treat person and the close date

    RefundUserRequest refundUserRequest = refundRequest.get();
    refundUserRequest.setClosedAt(new Date());
    refundUserRequest.setHandledBy(treatBy);
    refundUserRequest.setStatus(RefundStatus.REJECTED);
    refundRepo.save(refundUserRequest);

    Payment payment = refundUserRequest.getPaymentIntentId();

    mailService.sendRefundRequestEmail(
      payment.getBooking().getUser().getEmail(),
      "Refund Request Rejected",
      payment.getBooking().getUser(),
      payment.getBooking().getBookingPassengers().get(0),
      payment.getBooking().getFlight(),
      payment.getBooking().getSeat(),
      Double.parseDouble(refundUserRequest.getRefundAmount().toString()),
      refundUserRequest,
      RefundStatus.REJECTED
    );
  }

  @Override
  public void validateRefundUser(String paymentId) throws Exception {
    String handelBy = getCurrentUsername();

    User treatBy = userRepo
      .findByUsername(handelBy)
      .orElseThrow(
        () -> new Exception("Admin not found to treat the refund request !!!")
      );

    Optional<RefundUserRequest> refundRequest = refundRepo.findOne(
      (root, query, builder) -> builder.equal(root.get("id"), paymentId)
    );

    if (!refundRequest.isPresent()) {
      throw new Exception(
        String.format("The request with the id  %s not founded!", paymentId)
      );
    }

    // we need to refund the user
    String resultRefund = stripeService.processRefund(
      handelBy,
      refundRequest.get().getPaymentIntentId().getId()
    );

    if (resultRefund.isBlank()) {
      throw new Exception("Issue in the refound process");
    }
    // we will close the ticket or the request by setting the treat person and the close date

    RefundUserRequest refundUserRequest = refundRequest.get();
    refundUserRequest.setClosedAt(new Date());
    refundUserRequest.setHandledBy(treatBy);
    refundUserRequest.setStatus(RefundStatus.APPROVED);
    Payment payment = refundUserRequest.getPaymentIntentId();
    refundRepo.save(refundUserRequest);

    System.out.println(payment.getRefundUserRequest().getRefundAmount());

    mailService.sendRefundRequestEmail(
      payment.getBooking().getUser().getEmail(),
      "Refund Request Approved",
      payment.getBooking().getUser(),
      payment.getBooking().getBookingPassengers().get(0),
      payment.getBooking().getFlight(),
      payment.getBooking().getSeat(),
      Double.parseDouble(refundUserRequest.getRefundAmount().toString()),
      refundUserRequest,
      RefundStatus.COMPLETED
    );
  }
}
