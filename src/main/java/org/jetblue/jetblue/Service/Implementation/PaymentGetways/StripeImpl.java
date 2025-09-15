package org.jetblue.jetblue.Service.Implementation.PaymentGetways;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;
import org.jetblue.jetblue.Repositories.BookingPassengerPaymentRepo;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.NotificationServices.Mails.ReceiptMailService;
import org.jetblue.jetblue.Service.PaymentGetwaysServices.StripeService;
import org.jetblue.jetblue.Utils.EncryptInfoUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.jetblue.jetblue.Utils.UserUtils.getCurrentUsername;

@Service
@AllArgsConstructor
public class StripeImpl implements StripeService {

    private final EncryptInfoUtils encryptInfoUtils;
    private final PaymentRepo paymentRepo;
    private final BookingPassengerPaymentRepo bookingPassengerPaymentRepo;
    private final UserRepo userRepo;
    private final ReceiptMailService receiptMailService;


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
    public String processPayments(String flightId) throws StripeException, MessagingException {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null || currentUsername.isBlank()) {
            return "error: User not authenticated";
        }

        Optional<User> userOpt = userRepo.findByUsername(currentUsername);
        if (userOpt.isEmpty()) {
            return "error: User not found";
        }
        User user = userOpt.get();

        // Fetch all payments for this flight
        List<BookingPassengerPayment> passengerPayments =
                bookingPassengerPaymentRepo.findByUser_UsernameLikeIgnoreCaseAndFlight_FlightNumberLikeIgnoreCase(
                        user.getUsername(), flightId);

        if (passengerPayments.isEmpty()) {
            return "error: Payment not found";
        }

        // Sum all seat prices into total amount
        BigDecimal totalUsd = BigDecimal.ZERO;
        for (BookingPassengerPayment p : passengerPayments) {
            BigDecimal seatPrice = new BigDecimal(p.getPayment().getAmount());
            totalUsd = totalUsd.add(seatPrice);
        }
        long totalInCents = totalUsd.multiply(BigDecimal.valueOf(100)).longValue();

        // Pick a test card token (you can detect from user/card data)
        String typePaymentCard = "VISA";
        String tokenPaymentCard;
        switch (typePaymentCard) {
            case "MASTERCARD" -> tokenPaymentCard = "pm_card_mastercard";
            case "AMERICAN_EXPRESS" -> tokenPaymentCard = "pm_card_amex";
            case "DISCOVER" -> tokenPaymentCard = "pm_card_discover";
            case "DINERS_CLUB" -> tokenPaymentCard = "pm_card_diners";
            case "JCB" -> tokenPaymentCard = "pm_card_jcb";
            case "UNIONPAY" -> tokenPaymentCard = "pm_card_unionpay";
            default -> tokenPaymentCard = "pm_card_visa";
        }

        String emailReceipt = user.getEmail();

        // Build metadata for each seat
        Map<String, String> metadata = new HashMap<>();
        for (int i = 0; i < passengerPayments.size(); i++) {
            BookingPassengerPayment p = passengerPayments.get(i);
            String seatLabel = p.getPayment().getBooking().getSeat().getSeatLabel();
            String seatPrice = p.getPayment().getAmount();
            metadata.put(
                    "passenger :" + (i + 1), p.getPassenger().getFirstName() + " " + p.getPassenger().getLastName() + "\n Seat : " + seatLabel + " - $" + seatPrice
            );
        }

        // Create PaymentIntent with metadata
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalInCents)
                .setCurrency("usd")
                .setPaymentMethod(tokenPaymentCard)
                .addPaymentMethodType("card")
                .setReceiptEmail(emailReceipt)
                .setConfirm(true)
                .putAllMetadata(metadata)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        if (!"succeeded".equals(paymentIntent.getStatus())) {
            return "error: Payment failed with status " + paymentIntent.getStatus();
        }

        // Mark all passenger payments as completed in DB
        for (BookingPassengerPayment p : passengerPayments) {
            p.getPayment().setTransactionId(paymentIntent.getId());
            p.getPayment().setStatus(PaymentStatus.COMPLETED);
            paymentRepo.save(p.getPayment());
        }

        // Notify the user via email (handled by system automatically)
        receiptMailService.sendReceiptEmail(
                user.getEmail(),
                "Your JetBlue receipt for flight " + flightId,
                user,
                passengerPayments,
                paymentIntent
        );


        return "Payment succeeded, transactionId=" + paymentIntent.getId();
    }


    @Override
    public String processPayments(String username, Flight flight) throws StripeException {
        // first find the user and the passengers list associated with the user
        // find the flight and booking
        return null;
    }

    @Override
    public String processRefund(String username, String paymentIntentId,long refundedAmount) throws Exception {
        
        Payment paymentInfo = paymentRepo.findOne(
            (root, query, builder) -> builder.equal(root.get("id"), paymentIntentId)
    ).orElse(null);

    if (paymentInfo == null) {
        throw new Exception("Payment information not found !!");
    }
    String paymentTransactionId = paymentInfo.getTransactionId();

    RefundCreateParams params = RefundCreateParams.builder()
            .setPaymentIntent(paymentTransactionId) 
            .setAmount(refundedAmount)
            .build();

    Refund refund = Refund.create(params);

    return refund.getId(); 
    }

    
}
