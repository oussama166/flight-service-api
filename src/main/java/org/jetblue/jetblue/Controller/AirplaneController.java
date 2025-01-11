package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Airplane.AirplaneMapper;
import org.jetblue.jetblue.Mapper.Airplane.AirplaneRequest;
import org.jetblue.jetblue.Mapper.Airplane.AirplaneResponse;
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
    private final AirplaneMapper airplaneMapper;

    @PostMapping(
            value = "/createAirplane",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<AirplaneResponse> createAirplane(
            @RequestBody @Valid AirplaneRequest airplane
    ) {
        Airplane newAirplane = airplaneService.create(airplaneMapper.toAirplane(airplane));
        return ResponseEntity.ok(airplaneMapper.toAirplaneResponse(newAirplane));

    }

    @PutMapping(
            value = "/updateAirplane/{airplaneName}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> updateAirplane(
            @PathVariable String airplaneName,
            @RequestBody @Valid AirplaneRequest airplane
    ) {
        try {
            Airplane airplaneRes = airplaneService.update(airplaneName, airplaneMapper.toAirplane(airplane));
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
