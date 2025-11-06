package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.jetblue.jetblue.Mapper.Airport.AirportMapper;
import org.jetblue.jetblue.Mapper.Airport.AirportRequest;
import org.jetblue.jetblue.Mapper.Airport.AirportResponse;
import org.jetblue.jetblue.Models.DAO.Airport;
import org.jetblue.jetblue.Service.AirportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Airport", description = "Manage airports")
public class AirportController {
  // Integration
  private final AirportService airportService;

  // Constructor
  public AirportController(AirportService airportService) {
    this.airportService = airportService;
  }

  @PostMapping(value = "/setAirport", consumes = "application/json", produces = "application/json")
  @Operation(summary = "Create airport", description = "Create a new airport record")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Airport created"),
      @ApiResponse(responseCode = "400", description = "Bad request"),
  })
  public ResponseEntity<?> setAirport(@RequestBody AirportRequest airport) {
    try {
      Airport airportResponse = airportService.createAirport(
          AirportMapper.toAirport(airport));

      if (airportResponse != null) {
        return ResponseEntity.ok("Airport created successfully !!!");
      } else {
        return ResponseEntity.ok("Airport already exist !!!");
      }
    } catch (Exception e) {
      return ResponseEntity
          .badRequest()
          .body("Error occurred  at server level !!!");
    }
  }

  @PostMapping(value = "/setAirports", consumes = "application/json", produces = "application/json")
  @Operation(summary = "Create multiple airports", description = "Create multiple airports in batch")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Airports created"),
      @ApiResponse(responseCode = "400", description = "Bad request"),
  })
  public ResponseEntity<?> setAirports(
      @RequestBody List<AirportRequest> airports) {
    try {
      List<Airport> airportResponse = airportService.createAirports(airports);

      if (!airportResponse.isEmpty()) {
        return ResponseEntity.ok(airportResponse);
      } else {
        return ResponseEntity.ok("Airport already exist !!!");
      }
    } catch (Exception e) {
      return ResponseEntity
          .badRequest()
          .body("Error occurred  at server level !!!");
    }
  }

  // !- Refactor this to could accept only the field or fields wanted changes
  @PatchMapping(value = "/updateAirport/{code}", consumes = "application/json", produces = "application/json")
  @Operation(summary = "Update airport", description = "Update an airport by code")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Airport updated"),
      @ApiResponse(responseCode = "404", description = "Not found"),
  })
  public ResponseEntity<?> updateAirport(
      @RequestBody @Valid AirportRequest airport,
      @PathVariable String code) {
    Airport airportResponse = airportService.updateAirport(
        code,
        AirportMapper.toAirport(airport));

    try {
      if (airportResponse != null) {
        return ResponseEntity.ok("Airport updated successfully !!!");
      } else {
        return ResponseEntity.ok().body("Airport not found !!!");
      }
    } catch (Exception e) {
      return ResponseEntity
          .badRequest()
          .body("Error occurred  at server level !!!");
    }
  }

  @GetMapping(value = "/getAirport/{code}", produces = "application/json")
  @Operation(summary = "Get airport", description = "Retrieve airport details by code")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Airport returned"),
      @ApiResponse(responseCode = "400", description = "Not found"),
  })
  public ResponseEntity<?> getAirport(@PathVariable String code) {
    Airport airport = airportService.getAirport(code);
    try {
      if (airport != null) {
        return ResponseEntity.ok(AirportMapper.toAirportResponse(airport));
      } else {
        return ResponseEntity.badRequest().body("Airport not found !!!");
      }
    } catch (Exception e) {
      return ResponseEntity
          .badRequest()
          .body("Error occurred  at server level !!!");
    }
  }

  @GetMapping(value = "/getAirports", produces = "application/json")
  @Operation(summary = "List airports", description = "Retrieve all airports")
  public ResponseEntity<?> getAllAirports() {
    List<AirportResponse> airports = airportService.getAllAirports();

    return ResponseEntity.ok(airports);
  }

  @GetMapping(value = "/getAirportsByCountry/{country}", produces = "application/json")
  @Operation(summary = "Get airports by country", description = "Retrieve airports by country")
  public ResponseEntity<?> getAirportsByCountry(@PathVariable String country) {
    List<Airport> airports = airportService.getAirportsByCountry(country);
    return ResponseEntity.ok(airports);
  }

  @DeleteMapping(value = "/deleteAirport/{code}")
  @Operation(summary = "Delete airport", description = "Delete an airport by code")
  public ResponseEntity<?> deleteAirport(@PathVariable String code) {
    boolean deleted = airportService.deleteAirport(code);
    if (deleted) {
      return ResponseEntity.ok("Airport deleted successfully !!!");
    } else {
      return ResponseEntity.badRequest().body("Airport not found !!!");
    }
  }
}
