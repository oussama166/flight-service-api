package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.jetblue.jetblue.Service.FlightStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class FlightStatusController {

    private FlightStatusService flightStatusService;

    @PostMapping(
            name = "/setFlightStatus",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setFlightStatus(
            @RequestBody FlightStatus flightStatus
    ) {
        FlightStatus result = flightStatusService.setFlightStatus(flightStatus);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            name = "/getFlightStatus/{status}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> getFlightStatus(@PathVariable String status) {
        FlightStatus flightStatus = flightStatusService.getFlightStatus(status);
        if (flightStatus == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(flightStatus);
    }

    @PutMapping(
            name = "/updateFlightStatus/{status}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateFlightStatus(@PathVariable String status, @RequestBody FlightStatus flightStatus) {
        FlightStatus result = flightStatusService.updateFlightStatus(status, flightStatus);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(
            name="/deleteFlightStatus/{status}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> deleteFlightStatus(@PathVariable String status) {
        boolean result = flightStatusService.deleteFlightStatus(status);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Flight status was successfully deleted");
    }

}
