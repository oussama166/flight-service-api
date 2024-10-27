package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String airlineName;
    private String airlineCode;
    private String airlineUrl;
    private String airlineLogoLink;
    private int colFormation;
    private int rowFormation;

    @OneToMany
    @JoinColumn(name = "flights")
    private Set<Flight> flight;
}
