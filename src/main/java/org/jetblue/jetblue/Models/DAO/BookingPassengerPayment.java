package org.jetblue.jetblue.Models.DAO;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;

// one user need to be associted to one flight with different seats and passngers
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(
        name = "booking_passenger_payment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"flight_id", "user_id", "passenger_id"})
)
public class BookingPassengerPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @PrePersist
    public void prePersist() {
        if (paymentStatus == null) {
            paymentStatus = PaymentStatus.PENDING;
        }
    }
}
