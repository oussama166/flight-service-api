package org.jetblue.jetblue.Models.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSearch {
    private String Departure;
    private String Arrival;
    private String FlightStatus = "On Time";
}
