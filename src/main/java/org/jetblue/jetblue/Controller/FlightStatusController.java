package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.FlightStatus.FlightStatusMapper;
import org.jetblue.jetblue.Mapper.FlightStatus.FlightStatusRequest;
import org.jetblue.jetblue.Mapper.FlightStatus.FlightStatusResponse;
import org.jetblue.jetblue.Models.DAO.FlightStatus;
import org.jetblue.jetblue.Service.FlightStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class FlightStatusController {

    private FlightStatusService flightStatusService;

    @PostMapping(
            value = "/setFlightStatus",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setFlightStatus(
            @RequestBody FlightStatusRequest flightStatus
    ) {
        FlightStatus result = flightStatusService.setFlightStatus(FlightStatusMapper.toFlightStatus(flightStatus));
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping(
            value="/setListFlightsStatus",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setListFlightsStatus(
            @RequestBody List<String> flightStatus
    ) {
        try {
            boolean flightStatusChanged = flightStatusService.setListFlightStatus(flightStatus);
            if(flightStatusChanged) {
                return ResponseEntity.ok(flightStatus);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(
            value = "/getFlightStatus/{status}",
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
    @GetMapping(
            value = "/getFlightStatus",
            produces = "application/json"
    )
    public ResponseEntity<?> getFlightStatus() {
        try {
            List<FlightStatusResponse> fl = flightStatusService.getAllFlightStatus();
            return ResponseEntity.ok(fl);
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(
            value = "/updateFlightStatus/{status}",
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
            value="/deleteFlightStatus/{status}",
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
