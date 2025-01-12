package org.jetblue.jetblue.Models.DAO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String flightNumber;

    private int maxSeats;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double distance;
    private double duration;
    private double price;

    private int maxFirstClass;
    private int maxSecondClass;
    private int maxThirdClass;

    private int firstClassReserve;
    private int secondClassReserve;
    private int thirdClassReserve;

    private boolean firstClassAvailable = true;
    private boolean secondClassAvailable = true;
    private boolean thirdClassAvailable = true;

    private int firstClassReserved;
    private int secondClassReserved;
    private int thirdClassReserved;

    private boolean allowsBassinet;
    private boolean allowsUnaccompaniedMinors;
    private boolean providesChildMeals;

    // Relationships
    @ManyToOne(cascade = CascadeType.ALL)
    private Airplane airplane;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_dep")
    private Airport departure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airport_arr")
    private Airport arrival;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    @JsonBackReference("airline-flight") // Add this annotation
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_status_id")
    private FlightStatus status;

    @PrePersist
    public void generateFlightNumber() {
        this.flightNumber = UUID.randomUUID().toString();
    }
}