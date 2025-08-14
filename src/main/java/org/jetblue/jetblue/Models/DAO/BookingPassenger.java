package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "booking_passengers")
public class BookingPassenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    // Links to the main Booking record
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    // Links to the Passenger's profile
    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    // Links to the seat assigned for this specific passenger on this booking
    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = true)
    private Seat seat;

}