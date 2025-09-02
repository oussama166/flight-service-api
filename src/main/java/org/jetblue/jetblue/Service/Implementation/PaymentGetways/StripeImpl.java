package org.jetblue.jetblue.Service.Implementation.PaymentGetways;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Service.PaymentGetwaysServices.StripeService;
import org.jetblue.jetblue.Utils.EncryptInfoUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

            // getting the payment type and convert to stripe test token
            String typePaymentCard = card.getCardType().name();
            String tokenPaymentCard;
            switch (typePaymentCard) {
                case "VISA" -> tokenPaymentCard = "pm_card_visa";
                case "MASTERCARD" -> tokenPaymentCard = "pm_card_mastercard";
                case "AMERICAN_EXPRESS" -> tokenPaymentCard = "pm_card_amex";
                case "DISCOVER" -> tokenPaymentCard = "pm_card_discover";
                case "DINERS_CLUB" -> tokenPaymentCard = "pm_card_diners";
                case "JCB" -> tokenPaymentCard = "pm_card_jcb";
                case "UNIONPAY" -> tokenPaymentCard = "pm_card_unionpay";
                default -> tokenPaymentCard = "pm_card_visa"; // default to visa
            }

            String emailReceipt = paymentData.getBooking().getUser().getEmail();
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInCents)
                            .setCurrency("usd")
                            .setPaymentMethod(tokenPaymentCard)
                            .addPaymentMethodType("card")
                            .setReceiptEmail(emailReceipt)
                            .setConfirm(true)
                            .build();


            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (!paymentIntent.getStatus().equals("succeeded")) {
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

    @Override
    public String processPayments(List<String> paymentsIds) throws StripeException {
        List<Payment> payment = paymentRepo.findAllById(paymentsIds);
        if (payment.isEmpty()) {
            return "error: Payment not found";
        }



        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (int i = 0; i <= paymentsIds.size(); i++) {
            long seatPriceInCents = 5000L; // You can calculate different prices per seat if needed
            String seatId = String.format("Seat number : %d   X 1", i);
            String seatName = String.format("Seat name : %s", payment.get(i).getBooking().getSeat().getSeatLabel());

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmount(seatPriceInCents)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(seatId)
                                                    .setDescription(seatName)
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            lineItems.add(lineItem);
        }

        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(payment.get(0).getBooking().getUser().getEmail());

        // Add all line items
        for (SessionCreateParams.LineItem item : lineItems) {
            sessionBuilder.addLineItem(item);
        }
        SessionCreateParams sessionCreateParams = sessionBuilder.build();

        Session session = Session.create(sessionCreateParams);

        return session.getStatus();

    }

    @Override
    public String processPayments(String username, Flight flight) throws StripeException {
        // first find the user and the passengers list associated with the user
        // find the flight and booking
        return null;
    }
}
