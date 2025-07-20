package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Airline.AirlineMapper;
import org.jetblue.jetblue.Mapper.Airline.AirlineRequest;
import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Service.AirlineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@AllArgsConstructor
public class AirlineController {

    // Integration
    private AirlineService airlineService;

    @PostMapping(
            value = "/setAirline",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setAirline(
            @RequestBody @Valid AirlineRequest airline
    ) {
        AirlineResponse airlineRes = airlineService.setAirline(AirlineMapper.toAirline(airline));

        if (airlineRes == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(airlineRes);
    }


    @GetMapping(
            value = "/getAirline/{airline}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> getAirline(
            @PathVariable String airline
    ) {
        AirlineResponse airlineRes = airlineService.getAirline(airline);

        if (airlineRes == null) {
            return ResponseEntity.badRequest().body("Airline not found!!!");
        } else {
            return ResponseEntity.ok(airlineRes);
        }
    }

    @PutMapping(
            value = "/updateAirline/{airline}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateAirline(
            @PathVariable String airline,
            @RequestBody AirlineRequest newAirline
    ) {
        AirlineResponse airlineRes = airlineService.updateAirline(airline, newAirline);
        if (airlineRes == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(airlineRes);
        }
    }

    @DeleteMapping(
            value = "/deleteAirline/{airline}"
    )
    public ResponseEntity<?> deleteAirline(@PathVariable String airline) {
        boolean isDeleted = airlineService.deleteAirline(airline);
        if (isDeleted) {
            return ResponseEntity.ok("airline was successfully deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(
            value = "/getAirlineFlight"
    )
    public ResponseEntity<?> getAirlineFlight(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            @RequestParam String airline
    ) {
        Set<Flight> airlineRes = airlineService.getAllAirlineFlights(from, to, airline);

        if (airlineRes == null) {
            return ResponseEntity.badRequest().body("No flight in the this date");
        } else {
            return ResponseEntity.ok(airlineRes);
        }
    }


    @GetMapping(
            value = "/getAllAirlines"
    )
    public ResponseEntity<?> getAllAirlines() {
        return ResponseEntity.ok(airlineService.getAllAirlines());
    }

}

