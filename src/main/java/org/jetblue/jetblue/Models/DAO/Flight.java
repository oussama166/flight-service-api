package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String flightNumber;
    private Date departureTime;
    private Date arrivalTime;
    private double price;

    // Relation
    @OneToOne
    @JoinColumn(name="airport-dep")
    private Airport departure;
    @OneToOne
    @JoinColumn(name="airport-arr")
    private Airport arrival;

    @OneToOne
    @JoinColumn(name="airline-id")
    private Airline airline;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Seat> seats;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "Flight-status-id")
    private FlightStatus status;
}
