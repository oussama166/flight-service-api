package org.jetblue.jetblue.Service.Implementation.PaymentGetways;

import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Service.PaymentGetwaysServices.StripeService;
import org.jetblue.jetblue.Utils.EncryptInfoUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StripeImpl implements StripeService {

    private final EncryptInfoUtils encryptInfoUtils;
    private final PaymentRepo paymentRepo;

    @Override
    public String processPayment(String paymentId) {
        Optional<Payment> payment = paymentRepo.findById(paymentId);
        if (payment.isEmpty()) {
            return "error: Payment not found";
        }
        Payment paymentData = payment.get();

        try {
            if (!paymentData.getPaymentGateway().name().equals("STRIPE")) {
                return "error: Payment method is not STRIPE";
            }

            CreditCard card = paymentData.getCreditCard();

            // Decrypt card details (not really used with Stripe test token)
            String cardNumber = encryptInfoUtils.decrypt(card.getCardNumber());
            String cvv = encryptInfoUtils.decrypt(card.getCvv());
            String expDate = card.getExpirationDate(); // "2030-12-31"
            LocalDate exp = LocalDate.parse(expDate);
            String month = String.valueOf(exp.getMonthValue());
            String year = String.valueOf(exp.getYear());

            // Convert price to cents
            long amountInCents = (long) (paymentData.getBooking().getTotalPrice() * 100);

//            // 1. Create PaymentMethod directly with a test token
//            PaymentMethod paymentMethod = PaymentMethod.create(
//                    PaymentMethodCreateParams.builder()
//                            .setType(PaymentMethodCreateParams.Type.CARD)
//                            .setCard(PaymentMethodCreateParams.Token.builder().setToken("tok_visa").build())
//                            .putMetadata("order_id", payment.get().getId())
//                            .build()
//            );
//
//            // 2. Create PaymentIntent with that PaymentMethod
//            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                    .setAmount(amountInCents)
//                    .setCurrency("usd")
//                    .setPaymentMethod(paymentMethod.getId())
//                    .setConfirm(true)
//                    .build();
//
//            PaymentIntent paymentIntent = PaymentIntent.create(params);

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInCents)
                            .setCurrency("usd")
                            .setPaymentMethod("pm_card_visa")
                            .addPaymentMethodType("card")
                            .setConfirm(true)
                            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if(!paymentIntent.getStatus().equals("succeeded")){
                return "error: Payment failed with status " + paymentIntent.getStatus();
            }
            payment.get().setTransactionId(paymentIntent.getId());
            payment.get().setStatus(PaymentStatus.COMPLETED);
            paymentRepo.save(payment.get());
            return paymentIntent.getId();

        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

}
