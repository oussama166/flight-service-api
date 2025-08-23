package org.jetblue.jetblue.Models.DAO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String amount;
    private String currency;
    private String method;
    private String status;
    private String transactionId;
    private String orderId;

}
