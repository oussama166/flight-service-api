package org.jetblue.jetblue.Controller;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Service.FlightBaggageBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flight-baggage-booking")
@AllArgsConstructor
public class FlightBaggageBookingController {
  private FlightBaggageBookingService flightBaggageBookingService;

  @PostMapping("/book/baggage")
  public ResponseEntity<?> bookBaggageForFlight(
    @RequestParam int passengerId,
    @RequestParam String flightBaggageRef
  ) {
    flightBaggageBookingService.bookBaggageForFlight(
      passengerId,
      flightBaggageRef
    );
    return ResponseEntity.ok("Baggage booked successfully");
  }
}
