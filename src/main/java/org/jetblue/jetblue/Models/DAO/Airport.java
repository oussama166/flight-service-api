package org.jetblue.jetblue.Models.DAO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String code;
    private String name;
    private String location;
    @Column(unique = true)
    private double latitude;
    @Column(unique = true)
    private double longitude;


    @OneToMany(mappedBy = "departure", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("departure-flights")
    private List<Flight> departures;

    @OneToMany(mappedBy = "arrival", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("arrival-flights")
    private List<Flight> arrivals;

    @OneToMany(mappedBy = "airport")
    private List<StopOver> stopOvers = new ArrayList<>();
}
