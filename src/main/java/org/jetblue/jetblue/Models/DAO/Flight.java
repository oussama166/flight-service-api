package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String flightNumber;
    private int max_seats;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    @ManyToOne(cascade = CascadeType.ALL)
    private Airplane airplane;
    private int maxFirstClass;
    private int maxSecondClass;
    private int maxThirdClass;
    // this is being increment directly using the business logic
    private int FirstClassReserve;
    private int SecondClassReserve;
    private int ThirdClassReserve;

    // this is being increment directly using the booking by users
    private int FirstClassReserved;
    private int SecondClassReserved;
    private int ThirdClassReserved;

    // Relation
    @ManyToOne
    @JoinColumn(name = "airport-dep")
    private Airport departure;
    @ManyToOne
    @JoinColumn(name = "airport-arr")
    private Airport arrival;

    @ManyToOne
    @JoinColumn(name = "airline-id")
    private Airline airline;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Flight-status-id")
    private FlightStatus status;

    @OneToMany
    private List<Seat> seats;

    @PrePersist
    public void generateFlightNumber() {
        this.flightNumber = UUID.randomUUID().toString();
    }
}
