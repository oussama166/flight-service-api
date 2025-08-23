package org.jetblue.jetblue.Service.Implementation.PaymentGetways;

import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Service.PaymentGetwaysServices.StripeService;
import org.jetblue.jetblue.Utils.EncryptInfoUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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


            // Decrypt card details
            String cardNumber = encryptInfoUtils.decrypt(card.getCardNumber());
            String cvv = encryptInfoUtils.decrypt(card.getCvv());

            String expDate = card.getExpirationDate(); // "2030-12-31"
            LocalDate exp = LocalDate.parse(expDate); // parse ISO date
            String month = String.valueOf(exp.getMonthValue()); // 12
            String year = String.valueOf(exp.getYear()); // 2030

            // Convert price to cents
            long amountInCents = (long) (paymentData.getBooking().getTotalPrice() * 100);

            // **Important:** Instead of sending raw card info, create a Stripe Token first
            Map<String, Object> cardParams = new HashMap<>();
            cardParams.put("number", cardNumber);
            cardParams.put("exp_month", month);
            cardParams.put("exp_year", year);
            cardParams.put("cvc", cvv);

            Map<String, Object> tokenParams = new HashMap<>();
            tokenParams.put("card", cardParams);

            Token token = Token.create(tokenParams);

            // Create PaymentIntent using the token
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .setPaymentMethod(token.getCard().getId())
                    .setConfirm(true)
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return paymentIntent.getStatus();

        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }

    }
}
