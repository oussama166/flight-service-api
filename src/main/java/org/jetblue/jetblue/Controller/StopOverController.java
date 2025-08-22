package org.jetblue.jetblue.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.StopOver.StopOverRequest;
import org.jetblue.jetblue.Mapper.StopOver.StopOverResponse;
import org.jetblue.jetblue.Service.StopOverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class StopOverController {

    private final StopOverService stopOverService;

    @GetMapping(
            value = "/stopover/{flightNumber}",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> getStopOverDetails(@PathVariable String flightNumber) {
        try {
            List<StopOverResponse> stopOverDetails = stopOverService.getStopOverDetails(flightNumber);
            return ResponseEntity.ok(stopOverDetails);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving stopover details: " + e.getMessage());
        }
    }

    @PutMapping(
            value = "/stopover/reorder/{flightId}/{oldIndex}/{newIndex}",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> reorderStopOver(
            @PathVariable String flightId,
            @PathVariable int oldIndex,
            @PathVariable int newIndex) {
        try {
            stopOverService.reorderStopOver(flightId, oldIndex, newIndex);
            return ResponseEntity.ok("Stopover reordered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error reordering stopover: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/stopover/{flightId}/{airportId}",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> addStopOver(
            @PathVariable String flightId,
            @PathVariable String airportId,
            @RequestBody @Valid StopOverRequest stopOverDetails
    ) {
        try {
            stopOverService.addStopOver(flightId, airportId, stopOverDetails);
            return ResponseEntity.ok("Stopover added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding stopover: " + e.getMessage());
        }
    }

    // TODO : BACK TO THIS ENDPOINT TO ENHANCE LOGIC
    @DeleteMapping(
            value = "/stopover/{flightId}/{stopOrderIndex}",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<?> deleteStopOver(
            @PathVariable String flightId,
            @PathVariable int stopOrderIndex) {
        try {
            stopOverService.deleteStopOver(flightId, stopOrderIndex);
            return ResponseEntity.ok("Stopover deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting stopover: " + e.getMessage());
        }
    }
}