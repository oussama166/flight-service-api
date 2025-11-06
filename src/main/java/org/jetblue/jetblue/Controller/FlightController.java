package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Flight.FlightRequest;
import org.jetblue.jetblue.Mapper.Flight.FlightResponse;
import org.jetblue.jetblue.Mapper.Flight.FlightSearch.FlightSearchRequest;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(
  name = "Flight",
  description = "Manage flights: create, search and retrieve flight data"
)
public class FlightController {
  // Injection
  private final FlightService flightService;

  @PostMapping(
    value = "/setFlight",
    consumes = "application/json",
    produces = "application/json"
  )
  @Operation(
    summary = "Create a flight",
    description = "Create a new flight with provided details"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Flight created or returned"
      ),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
    }
  )
  public ResponseEntity<?> setFlight(@RequestBody @Valid FlightRequest flight) {
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
        flight.flightStatus(),
        flight.pricingType()
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
  @Operation(
    summary = "Create multiple flights",
    description = "Create multiple flights in batch"
  )
  @ApiResponses(
    {
      @ApiResponse(responseCode = "200", description = "Flights created"),
      @ApiResponse(responseCode = "500", description = "Server error"),
    }
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
  @Operation(
    summary = "Get flight by number",
    description = "Retrieve a flight by its flight number"
  )
  @ApiResponses(
    {
      @ApiResponse(responseCode = "200", description = "Flight returned"),
      @ApiResponse(responseCode = "404", description = "Not found"),
    }
  )
  public ResponseEntity<?> getFlight(@PathVariable String flightNumber) {
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

  @GetMapping(value = "/getFlight", produces = "application/json")
  @Operation(
    summary = "Get flight by number and airline",
    description = "Retrieve flight matching given flight number and airline"
  )
  @ApiResponses(
    {
      @ApiResponse(responseCode = "200", description = "Flight returned"),
      @ApiResponse(responseCode = "404", description = "Not found"),
    }
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
  @Operation(
    summary = "Search flights by departure/arrival",
    description = "Search flights by departure, arrival and status"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "List of flights returned"
      ),
      @ApiResponse(responseCode = "404", description = "No flights found"),
    }
  )
  public ResponseEntity<?> getFlights(
    @RequestBody @Valid FlightSearchRequest flightSearch
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
