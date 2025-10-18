package org.jetblue.jetblue.Controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Seat.SeatResponse;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.DTO.SeatCreationRequest;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SeatController {
  private final SeatService seatService;
  private final FlightRepo flightRepo;

  @PostMapping(
    value = "/createSeat",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> createSeat(@RequestBody SeatCreate seat) {
    try {
      // Create a seat using the seatService
      SeatResponse savedSeat = seatService.createSeat(seat);

      // Return the created seat with HTTP status 201 (Created)
      return ResponseEntity.status(HttpStatus.CREATED).body(savedSeat);
    } catch (Exception e) {
      // If there's an error, return a 400 (Bad Request) with the error message
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(
    value = "/createSeats",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> createSeats(
    @RequestBody SeatCreationRequest seatCreationRequest
  ) {
    try {
      // Create seats using the seatService
      List<SeatResponse> seats = seatService.createSeats(seatCreationRequest);

      // If the list is not empty, return the seats created with HTTP status 201 (Created)
      if (!seats.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seats);
      }

      // If no seats were created (unlikely, but a safeguard), return 404 (Not Found)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    } catch (Exception e) {
      // If there's an error, return a 400 (Bad Request) with the error message
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping(
    value = "/updateSeat",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> updateSeat(
    @RequestParam("seatLabel") String seatLabel,
    @RequestParam("flightNumber") String flightNumber,
    @RequestBody Seat seat
  ) {
    try {
      Seat seatResp = seatService.updateSeat(seatLabel, seat, flightNumber);
      if (seatResp == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(seat);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping(value = "/getSeat/{seatFlag}", produces = "application/json")
  public ResponseEntity<?> getSeat(@PathVariable String seatFlag) {
    try {
      SeatResponse seat = seatService.getSeat(seatFlag);
      if (seat == null) {
        return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(seat);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping(
    value = "/getSeats/{flightNumber}",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> getSeats(@PathVariable String flightNumber) {
    System.out.println(flightNumber);
    List<SeatResponse> seats = seatService.getAllSeats(flightNumber);

    if (!seats.isEmpty()) {
      return ResponseEntity.ok(seats);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }
}
