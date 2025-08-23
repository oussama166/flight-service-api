package org.jetblue.jetblue.Models.DAO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.jetblue.jetblue.Models.ENUM.CardType;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(length = 512) // allow long RSA encrypted value
    private String cardNumber;

    @Column(length = 512) // SHA-256 hash (Base64 ~ 44 chars, but extra margin is good)
    private String hashedCardNumber;

    @Column(length = 4) // last 4 digits only
    private String lastFourDigits;
    private String cardHolderName;
    private String expirationDate;
    @Column(length = 512) // allow long RSA encrypted value
    private String cvv;
    private String billingAddress;
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

}
