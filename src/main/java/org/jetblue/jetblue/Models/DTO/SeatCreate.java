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
    private boolean LapEnfant;
    private boolean SpecialTrait;
    private boolean Sold;
    private int col;
    private int row;

}
