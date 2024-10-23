package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DTO.FlightPostDTO;
import org.jetblue.jetblue.Models.DTO.FlightSearch;
import org.jetblue.jetblue.Service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    flight.getMaxSeats(),
                    flight.getDeparture(),
                    flight.getArrival(),
                    flight.getAirline(),
                    flight.getAirplane(),
                    flight.getMaxFirst(),
                    flight.getMaxSecond(),
                    flight.getMaxThird(),
                    flight.getFlightStatus()
            );
            if (flightResp == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flightResp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(
            value = "/getFlight/{flightNumber}",
            produces = "application/json"
    )
    public ResponseEntity<?> getFlight(
            @PathVariable String flightNumber
    ) {
        try {
            Flight flightResp = flightService.getFlight(flightNumber);
            if (flightResp == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flightResp);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(
            value = "/getFlight",
            produces = "application/json"
    )
    public ResponseEntity<?> getFlights(
            @RequestParam("flightNumber") String flightNumber,
            @RequestParam("airline") String airline
    ) {
        try {
            Flight flightResp = flightService.getFlight(flightNumber, airline);
            if (flightResp == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flightResp);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(
            value = "/getFlightByArrDes",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> getFlights(@RequestBody FlightSearch flightSearch) {
        try {
            System.out.println(flightSearch.getDeparture());
            List<Flight> flights = flightService.getFlight(flightSearch.getDeparture(), flightSearch.getArrival(), flightSearch.getFlightStatus());
            if (flights.isEmpty()) { // Check if the list is empty
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
