package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DTO.FlightPostDTO;
import org.jetblue.jetblue.Service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class flightController {

    // Injection
    private final FlightService flightService;

    @PostMapping(
            value = "/setFlight",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setFlight(
            @RequestBody FlightPostDTO flight
    ) {
        try {
            Flight flightResp = flightService.setFlight(
                    flight.getDepartureTime(),
                    flight.getArrivalTime(),
                    flight.getPrice(),
                    flight.getDeparture(),
                    flight.getArrival(),
                    flight.getAirline(),
                    flight.getAirplane(),
                    flight.getFlightStatus()
            );
            if(flightResp == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flightResp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
