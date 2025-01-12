package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Flight.FlightMapper;
import org.jetblue.jetblue.Mapper.Flight.FlightRequest;
import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
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
            @RequestBody @Valid FlightRequest flight
    ) {
        try {
            FlightResponse flightResp = flightService.setFlight(
                    flight.departureTime(),
                    flight.arrivalTime(),
                    flight.price(),
                    flight.maxSeats(),
                    flight.departure(),
                    flight.arrival(),
                    flight.airline(),
                    flight.airplane(),
                    flight.maxFirst(),
                    flight.maxSecond(),
                    flight.maxThird(),
                    flight.flightStatus()
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
            return ResponseEntity.ok(FlightMapper.toFlightResponse(flightResp));
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
            List<FlightResponse> flights = flightService.getFlight(
                    flightSearch.getDeparture(),
                    flightSearch.getArrival(),
                    flightSearch.getFlightStatus()
            );
            if (flights.isEmpty()) { // Check if the list is empty
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
