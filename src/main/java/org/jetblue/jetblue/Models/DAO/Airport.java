package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique=true)
    private String code;
    private String name;
    private String location;
    @Column(unique=true)
    private double latitude;
    @Column(unique=true)
    private double longitude;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="flight_id")
    private List<Flight> flight;
}
