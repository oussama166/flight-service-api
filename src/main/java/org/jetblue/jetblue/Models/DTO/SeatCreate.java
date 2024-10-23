package org.jetblue.jetblue.Models.DTO;

import lombok.*;
import org.jetblue.jetblue.Models.ENUM.SeatType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SeatCreate {
    private String flag;
    private double price;
    private SeatType seatType;
    private String flightNumber;
}
