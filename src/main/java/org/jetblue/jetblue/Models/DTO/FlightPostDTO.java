package org.jetblue.jetblue.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlightPostDTO {
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int maxSeats;
    private String departure;
    private String arrival;
    private String airline;
    private String airplane;
    private int maxFirst;
    private int maxSecond;
    private int maxThird;
    private String flightStatus;

}
