package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Models.ENUM.SeatType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatNumber;
    private String flag;
    private double price;
    private boolean isAvailable = true;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    // Relation

    @ManyToOne
    @JoinColumn(name = "seats")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "seat-booking")
    private Booking seatbooking;


    @PrePersist
    public void prePersistFlag(){
        this.flag = this.getSeatNumber() + "-" + this.flight.getFlightNumber();
    }
}
