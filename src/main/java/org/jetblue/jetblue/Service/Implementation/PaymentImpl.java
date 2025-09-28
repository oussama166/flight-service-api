package org.jetblue.jetblue.Service.Implementation;

import static org.jetblue.jetblue.Utils.UserUtils.getCurrentUsername;
import static org.jetblue.jetblue.Utils.UserUtils.getRoleConnectedUser;
import static org.jetblue.jetblue.Utils.UserUtils.userIsAllowed;

import java.util.List;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Payment.PaymentMapper;
import org.jetblue.jetblue.Mapper.Payment.PaymentResponse;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Service.PaymentService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentImpl implements PaymentService {
  private PaymentRepo paymentRepo;

  @Override
  public List<PaymentResponse> getAllUserPayment() {
    String username = userIsAllowed("User");

    List<Payment> allPaymentsUser = paymentRepo.findAll(
      (root, query, builder) ->
        builder.and(
          builder.equal(
            root.get("booking").get("user").get("username"),
            username
          ),
          builder.isNull(root.get("refundUserRequest"))
        )
    );

    return allPaymentsUser
      .stream()
      .map(PaymentMapper::toPaymentResponse)
      .toList();
  }

  @Override
  public List<PaymentResponse> getAllUserPaymentCriteria(
    String status,
    String paymentMethod
  ) {
    String username = userIsAllowed("User");

    List<Payment> allPaymentsUser = paymentRepo.findAll(
      (root, query, builder) -> {
        var predicates = builder.conjunction();

        if (username != null) {
          predicates =
            builder.and(
              predicates,
              builder.equal(
                root.get("booking").get("user").get("username"),
                username
              )
            );
        }

        if (status != null && !status.isEmpty()) {
          predicates =
            builder.and(predicates, builder.equal(root.get("status"), status));
        }

        if (paymentMethod != null && !paymentMethod.isEmpty()) {
          predicates =
            builder.and(
              predicates,
              builder.equal(root.get("paymentGateway"), paymentMethod)
            );
        }

        predicates =
          builder.and(
            predicates,
            builder.isNull(root.get("refundUserRequest"))
          );

        return predicates;
      }
    );

    return allPaymentsUser
      .stream()
      .map(PaymentMapper::toPaymentResponse)
      .toList();
  }
}
