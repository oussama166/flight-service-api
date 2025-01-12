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
@Table(name = "seat"
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = {"flight_id", "seat_number"}),
//        }
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "seat_label", nullable = false)
    private String seatLabel;

    @Column(name = "flag", nullable = false)
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

    @Column(name = "seat_col", nullable = false)
    private int col;

    // Change 'row' to 'seat_row' to avoid reserved keyword
    @Column(name = "seat_row", nullable = false)
    private int row;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    @ManyToOne()
    @JoinColumn(name = "flight_id")
    @JsonBackReference("flight-seats")
    private Flight flight;


    @ManyToOne()
    @JoinColumn(name = "seat_booking_id")
    private Booking seatBooking;

    @ManyToOne()
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;

    @PrePersist
    public void prePersistFlag() {
        if (this.flight != null) {
            this.flag = this.airplane.getId() + "-" + this.seatLabel + "-" + this.flight.getFlightNumber() + "-" + System.currentTimeMillis();
        }
    }
}