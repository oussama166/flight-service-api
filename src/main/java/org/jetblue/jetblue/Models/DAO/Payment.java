package org.jetblue.jetblue.Models.DAO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.jetblue.jetblue.Models.ENUM.PaymentMethod;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;
import org.jetblue.jetblue.Models.ENUM.PaymentGateway;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String amount;
    private String currency;
    // for the moment the default is CREDIT_CARD
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    // for the moment the default is STRIPE
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;
    private String transactionId;
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
