package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import java.sql.Types;
import java.util.List;
import java.util.UUID;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    @JdbcTypeCode(Types.VARCHAR)
    private UUID bookingId;
    private double totalPrice;


    // Relation

    @ManyToOne
    @JoinColumn(name = "user-id")
    private User user;

    @OneToOne
    @JoinColumn(name = "flight-id")
    private Flight flight;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="flight-seats")
    private List<Seat> seats;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking-status-id")
    private BookingStatus status;


}
