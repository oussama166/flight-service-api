package org.jetblue.jetblue.Service.Implementation;

import static org.jetblue.jetblue.Utils.PriceEngine.refundPenalty;
import static org.jetblue.jetblue.Utils.UserUtils.getCurrentUsername;
import static org.jetblue.jetblue.Utils.UserUtils.getRoleConnectedUser;
import static org.jetblue.jetblue.Utils.UserUtils.userIsAllowed;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
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

    RefundStatus status = payment.getRefundUserRequest().getStatus();
    if (refundRequest == null) {
      throw new IllegalStateException(
        "No refund request associated with this payment."
      );
    }

    if (refundRequest.getStatus() == RefundStatus.PENDING) {
      mailService.sendRefundRequestEmail(
        payment.getBooking().getUser().getEmail(),
        "Refund Request Submitted",
        payment.getBooking().getUser(),
        payment.getBooking().getBookingPassengers().get(0),
        payment.getBooking().getFlight(),
        payment.getBooking().getSeat(),
        penalty,
        refundRequest,
        status
      ); // process it
      return "success: Refund request submitted successfully\n Mail send successful";
    }
    return "success: Refund request submitted successfully\n Mail send with issue";
  }

  @Override
  public List<RefundUserRequestResponse> getRefundPerUser(String userName) {
    userIsAllowed("Admin");
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
    userIsAllowed("Admin");
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
  public void declineRefundUser(String paymentId, String rejectionReason)
    throws Exception {
    String handelBy = userIsAllowed("Admin");

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
    refundUserRequest.setRejectionReason(rejectionReason);
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

  @Transactional
  @Override
  public void validateRefundUser(String paymentId) throws Exception {
    String handelBy = getCurrentUsername();
    log.info(getRoleConnectedUser().toString());
    if (!getRoleConnectedUser().contains("Admin")) {
      throw new SecurityException(
        "Access denied : you need to be admin to validate refund!!!!"
      );
    }

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
    String resultRefund = "";
    String idPayment = refundRequest.get().getPaymentIntentId().getId();

    try {
      resultRefund =
        stripeService.processRefund(
          handelBy,
          idPayment,
          refundRequest
            .get()
            .getRefundAmount()
            .multiply(BigDecimal.valueOf(100))
            .longValue()
        );
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

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
    BigDecimal refundedPercent = refundRequest
      .get()
      .getRefundAmount()
      .divide(refundRequest.get().getAmount(), 4, RoundingMode.HALF_UP);

    double refundedPercentDouble = refundedPercent.doubleValue();

    mailService.sendRefundRequestEmail(
      payment.getBooking().getUser().getEmail(),
      "Refund Request Approved",
      payment.getBooking().getUser(),
      payment.getBooking().getBookingPassengers().get(0),
      payment.getBooking().getFlight(),
      payment.getBooking().getSeat(),
      refundedPercentDouble,
      refundUserRequest,
      RefundStatus.COMPLETED
    );
  }
}
