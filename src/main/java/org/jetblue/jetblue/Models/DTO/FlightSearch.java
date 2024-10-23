package org.jetblue.jetblue.Models.DTO;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FlightSearch {
    private String Departure;
    private String Arrival;
    private String FlightStatus = "On Time";

}
