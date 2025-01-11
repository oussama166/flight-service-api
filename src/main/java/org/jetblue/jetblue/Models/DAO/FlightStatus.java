package org.jetblue.jetblue.Models.DAO;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String status;

    // Relation
    @OneToMany()
    @JoinColumn(name = "flights")
    private List<Flight> flight;
}
