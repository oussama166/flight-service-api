package org.jetblue.jetblue.Models.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Models.DAO.Flight;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class FlightItinerary {
    private List<FlightResponse> flights;
    private double totalPrice;
}
