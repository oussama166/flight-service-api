package org.jetblue.jetblue.Models.DAO;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(unique = false)
    private double latitude;
    @Column(unique = false)
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
