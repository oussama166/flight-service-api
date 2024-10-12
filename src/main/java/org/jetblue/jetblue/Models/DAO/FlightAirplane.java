package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightAirplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int assignedSeats;
    private boolean hasEconomic;
    private boolean hasFirst;
    private boolean hasSecond;



    // relation
    @ManyToOne
    @JoinColumn(name="flight_id")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name="airpline_id")
    private Airplane airplane;
}
