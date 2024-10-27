package org.jetblue.jetblue.Controller;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.DTO.SeatCreationRequest;
import org.jetblue.jetblue.Service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SeatController {


    private final SeatService seatService;

    @PostMapping(
            value = "/createSeat",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> createSeat(
            @RequestBody SeatCreate seat
    ) {
        try {
            // Create a seat using the seatService
            Seat savedSeat = seatService.createSeat(seat);

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
            List<Seat> seats = seatService.createSeats(
                    seatCreationRequest.getMaxSeatNumber(),
                    seatCreationRequest.getPrice(),
                    seatCreationRequest.getSeatType(),
                    seatCreationRequest.getAirplaneName(),
                    seatCreationRequest.getRowStart()
            );

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
            @RequestParam("seatId") int seatId,
            @RequestParam("flightNumber") String flightNumber,
            @RequestBody Seat seat
    ) {
        try {
            Seat seatResp = seatService.updateSeat(seatId, seat, flightNumber);
            if (seatResp == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(seat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(
            name = "/getSeat",
            produces = "application/json"
    )
    public ResponseEntity<?> getSeat(
            @PathVariable int seatId
    ) {
        try {
            Seat seat = seatService.getSeat(seatId);
            if (seat == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(seat);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
