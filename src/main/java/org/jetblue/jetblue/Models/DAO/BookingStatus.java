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
public class BookingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String status;

    // Relation
    @OneToMany
    @JoinColumn(name = "status")
    private List<Booking> booking;
}
