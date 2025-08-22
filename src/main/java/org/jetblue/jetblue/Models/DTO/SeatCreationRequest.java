package org.jetblue.jetblue.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Models.ENUM.SeatType;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatCreationRequest {
    private int maxSeatNumber;
    private double price;
    private SeatType seatType;
    private String airplaneName;
    private int rowStart;
}
