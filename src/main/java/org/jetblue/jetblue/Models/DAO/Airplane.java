package org.jetblue.jetblue.Models.DAO;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String name;
    private int maxSeat;
    private String airplaneImageUrl;


    // relation
    @OneToMany
    private List<Seat> seats;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Flight> flight;


}
