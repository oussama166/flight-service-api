package org.jetblue.jetblue.Models.DAO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("airline-flight")
    private List<Flight> flights;
}
