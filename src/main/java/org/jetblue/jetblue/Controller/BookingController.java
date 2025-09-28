package org.jetblue.jetblue.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Booking.BookingFeeResponse;
import org.jetblue.jetblue.Mapper.Booking.BookingRequest;
import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Mapper.Passenger.PassengersRequest;
import org.jetblue.jetblue.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(
  name = "Booking Controller",
  description = "Handles booking related operations"
)
public class BookingController {
  // Inject the imp
  private final BookingService bookingService;

  @PostMapping(
    value = "setBooking/",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> setBooking(@RequestBody BookingRequest booking) {
    BookingResponse bookingRes = bookingService.setBooking(
      booking.UserName(),
      booking.FlightNumber(),
      booking.seatLabel()
    );

    if (bookingRes == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(bookingRes);
  }

  @PostMapping(
    value = "setBookingList/{username}",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> setBookingList(
    @PathVariable(value = "username") String userName,
    @RequestBody PassengersRequest passengersSeats
  ) {
    BookingFeeResponse bookingRes = bookingService.setBookings(
      userName,
      passengersSeats.passengers(),
      passengersSeats.flightNumber()
    );
    if (bookingRes == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(bookingRes);
  }

  @GetMapping(
    value = "getUserBooking/{username}",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> getUserBooking(@PathVariable String username) {
    List<BookingResponse> bookingList = bookingService.getUserBookings(
      username
    );
    return ResponseEntity.ok(bookingList);
  }
}
