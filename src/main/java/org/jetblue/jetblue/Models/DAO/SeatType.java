package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SeatType")
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String seatType;
}
