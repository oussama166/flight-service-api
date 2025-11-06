package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Airline.AirlineMapper;
import org.jetblue.jetblue.Mapper.Airline.AirlineRequest;
import org.jetblue.jetblue.Mapper.Airline.AirlineResponse;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Service.AirlineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "Airline", description = "Manage airlines and their flights")
public class AirlineController {
  // Integration
  private AirlineService airlineService;

  @PostMapping(
    value = "/setAirline",
    consumes = "application/json",
    produces = "application/json"
  )
  @Operation(
    summary = "Create airline",
    description = "Create a new airline record"
  )
  @ApiResponses(
    { @ApiResponse(responseCode = "200", description = "Airline created") }
  )
  public ResponseEntity<?> setAirline(
    @RequestBody @Valid AirlineRequest airline
  ) {
    AirlineResponse airlineRes = airlineService.setAirline(
      AirlineMapper.toAirline(airline)
    );

    if (airlineRes == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(airlineRes);
  }

  @PostMapping(
    value = "/setAirlines",
    consumes = "application/json",
    produces = "application/json"
  )
  @Operation(
    summary = "Create multiple airlines",
    description = "Create multiple airlines in batch"
  )
  @ApiResponses(
    { @ApiResponse(responseCode = "200", description = "Airlines created") }
  )
  public ResponseEntity<?> setAirlines(
    @RequestBody @Valid Set<AirlineRequest> airlines
  ) {
    Set<AirlineResponse> airlineRes = airlineService.setAirlines(airlines);

    if (airlineRes.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(airlineRes);
    }
  }

  @GetMapping(
    value = "/getAirline/{airline}",
    consumes = "application/json",
    produces = "application/json"
  )
  @Operation(
    summary = "Get airline",
    description = "Retrieve an airline by its code/name"
  )
  @ApiResponses(
    {
      @ApiResponse(responseCode = "200", description = "Airline returned"),
      @ApiResponse(responseCode = "400", description = "Airline not found"),
    }
  )
  public ResponseEntity<?> getAirline(@PathVariable String airline) {
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
  @Operation(
    summary = "Update airline",
    description = "Update details for an existing airline"
  )
  @ApiResponses(
    { @ApiResponse(responseCode = "200", description = "Airline updated") }
  )
  public ResponseEntity<?> updateAirline(
    @PathVariable String airline,
    @RequestBody AirlineRequest newAirline
  ) {
    AirlineResponse airlineRes = airlineService.updateAirline(
      airline,
      newAirline
    );
    if (airlineRes == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(airlineRes);
    }
  }

  @DeleteMapping(value = "/deleteAirline/{airline}")
  @Operation(
    summary = "Delete airline",
    description = "Delete an airline by code/name"
  )
  @ApiResponses(
    { @ApiResponse(responseCode = "200", description = "Airline deleted") }
  )
  public ResponseEntity<?> deleteAirline(@PathVariable String airline) {
    boolean isDeleted = airlineService.deleteAirline(airline);
    if (isDeleted) {
      return ResponseEntity.ok("airline was successfully deleted");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/getAirlineFlight")
  @Operation(
    summary = "Get airline flights",
    description = "Get flights for an airline between two dates"
  )
  @ApiResponses(
    { @ApiResponse(responseCode = "200", description = "Flights returned") }
  )
  public ResponseEntity<?> getAirlineFlight(
    @RequestParam LocalDateTime from,
    @RequestParam LocalDateTime to,
    @RequestParam String airline
  ) {
    Set<Flight> airlineRes = airlineService.getAllAirlineFlights(
      from,
      to,
      airline
    );

    if (airlineRes == null) {
      return ResponseEntity.badRequest().body("No flight in the this date");
    } else {
      return ResponseEntity.ok(airlineRes);
    }
  }

  @GetMapping(value = "/getAllAirlines")
  @Operation(summary = "List airlines", description = "Retrieve all airlines")
  public ResponseEntity<?> getAllAirlines() {
    return ResponseEntity.ok(airlineService.getAllAirlines());
  }
}
