package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Models.ENUM.ReasonStatus;
import org.jetblue.jetblue.Models.ENUM.RefundStatus;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RefundUserRequest {
    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private ReasonStatus reasonTitle;
    @Column(length = 1000)
    private String reasonDescription;
    @OneToOne(fetch = FetchType.EAGER)
    private Payment paymentIntentId;
    private BigDecimal amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private RefundStatus status = RefundStatus.PENDING;
    private BigDecimal refundAmount;
    private Date createdAt = new Date(System.currentTimeMillis());

}
