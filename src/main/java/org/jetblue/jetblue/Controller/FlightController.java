package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Flight.FlightRequest;
import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Flight.FlightSearch.FlightSearchRequest;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class FlightController {

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

    @PostMapping(
            value = "/setFlights",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setFlights(
            @RequestBody @Valid List<FlightRequest> flights
    ) {
        try {
            List<FlightResponse> flightResponses = flightService.setFlights(flights);
            if (flightResponses.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flightResponses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
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
            FlightResponse flightResp = flightService.getFlight(flightNumber);
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
    public ResponseEntity<?> getFlights(
            @RequestBody
            @Valid
            FlightSearchRequest flightSearch
    ) {
        try {
            List<FlightResponse> flights = flightService.getFlight(
                    flightSearch.departure(),
                    flightSearch.arrival(),
                    flightSearch.statusFlight()
            );
            if (flights.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
