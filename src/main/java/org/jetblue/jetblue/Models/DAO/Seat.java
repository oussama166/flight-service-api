package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatNumber;
    private double price;
    private boolean isAvailable = true;

    // Relation
    @OneToOne
    @JoinColumn(name = "seat-type-id")
    private SeatType seatClass;

    @ManyToOne
    @JoinColumn(name = "flight-seat-id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name="seat-booking")
    private Booking seatBooking;
}
