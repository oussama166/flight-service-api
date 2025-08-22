package org.jetblue.jetblue.Models.DAO;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String name;
    private int maxSeat;
    private String airplaneImageUrl;


    // relation
    @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Seat> seats;

    @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("airplane-flight")
    private List<Flight> flight;



}
