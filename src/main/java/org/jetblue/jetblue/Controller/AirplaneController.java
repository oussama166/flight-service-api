package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.jetblue.jetblue.Service.AirplaneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class AirplaneController {

    // inject the service
    private final AirplaneService airplaneService;

    @PostMapping(
            value = "/createAirplane",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> createAirplane(
            @RequestBody Airplane airplane
    ) {
        try {
            Airplane newAirplane = airplaneService.create(airplane);
            return ResponseEntity.ok(Objects.requireNonNullElse(newAirplane, HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(
            value = "/updateAirplane/{airplaneName}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateAirplane(
            @PathVariable String airplaneName,
            @RequestBody Airplane airplane
    ) {
        try {
            Airplane airplaneRes = airplaneService.update(airplaneName, airplane);
            return ResponseEntity.ok(Objects.requireNonNullElse(airplaneRes, HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(
            value = "/getAirplane/{airplaneName}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> getAirplane(@PathVariable String airplaneName) {
        try {
            Airplane airplane = airplaneService.getAirplane(airplaneName);
            return ResponseEntity.ok(Objects.requireNonNullElse(airplane, HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @GetMapping(
            value = "/getAirplanes",
            produces = "application/json"
    )
    public ResponseEntity<?> getAllAirplanes() {
        try {
            List<Airplane> airplanes = airplaneService.getAllAirplanes();
            return ResponseEntity.ok(Objects.requireNonNullElse(airplanes, HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(
            value = "/deleteAirplane/{airplaneName}",
            consumes = "application/json",
            produces = "application/json"

    )
    public ResponseEntity<?> deleteAirplane(@PathVariable String airplaneName) {
        try {
            Airplane airplaneDeleted = airplaneService.delete(airplaneName);
            return ResponseEntity.ok(Objects.requireNonNullElse(airplaneDeleted, HttpStatus.NO_CONTENT));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
