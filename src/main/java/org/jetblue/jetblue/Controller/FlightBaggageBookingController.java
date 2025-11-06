package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
  name = "FlightBaggageBooking",
  description = "Book baggage offers for passengers on flights"
)
public class FlightBaggageBookingController {
  private FlightBaggageBookingService flightBaggageBookingService;

  @PostMapping("/book/baggage")
  @Operation(
    summary = "Book baggage plan for passenger",
    description = "Associate a flight baggage offer with a passenger booking"
  )
  @ApiResponses(
    {
      @ApiResponse(
        responseCode = "200",
        description = "Baggage booked successfully"
      ),
    }
  )
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
