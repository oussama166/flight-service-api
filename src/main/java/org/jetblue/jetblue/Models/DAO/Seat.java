package org.jetblue.jetblue.Models.DAO;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.jetblue.jetblue.Models.ENUM.SeatType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "seat",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"flight_id", "seat_number"}),
                @UniqueConstraint(columnNames = {"flag"})
        }
)
public class Seat {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Column(name = "seat_label", nullable = false)
    private String seatLabel;

    @Column(name = "flag", unique = true)
    private String flag;

    @Column(nullable = false)
    private double price;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "is_special_trait")
    private boolean isSpecialTrait = false;

    @Column(name = "is_sold")
    private boolean isSold = false;

    @Column(name = "is_leap_enfant_seat")
    private boolean isLeapEnfantSeat = false;

    @Column(nullable = false)
    private int col;

    // Change 'row' to 'seat_row' to avoid reserved keyword
    @Column(name = "seat_row", nullable = false)
    private int row;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id")
    @JsonBackReference("flight-seats")
    private Flight flight;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_booking_id")
    private Booking seatBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @PrePersist
    public void prePersistFlag() {
        if (this.flight != null) {
            this.flag = this.seatNumber + "-" + this.seatType + "-" + this.flight.getFlightNumber();
        }
    }
}