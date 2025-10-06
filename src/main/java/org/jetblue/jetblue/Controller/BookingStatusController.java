package org.jetblue.jetblue.Controller;

import java.util.List;
import org.jetblue.jetblue.Mapper.BookingStatus.BookingStatusRequest;
import org.jetblue.jetblue.Models.DAO.BookingStatus;
import org.jetblue.jetblue.Repositories.BookingStatusRepo;
import org.jetblue.jetblue.Service.BookingStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookingStatusController {
  private final BookingStatusService bookingStatusService;

  public BookingStatusController(
    BookingStatusService bookingStatusService,
    BookingStatusRepo bookingStatusRepo
  ) {
    this.bookingStatusService = bookingStatusService;
  }

  @PostMapping(
    value = "/setBookingStatus",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> setBookingStatus(
    @RequestBody BookingStatusRequest bookingStatus
  ) {
    try {
      BookingStatus bookingStat = bookingStatusService.setBookingStatus(
        bookingStatus
      );
      if (bookingStat == null) return ResponseEntity.ok(
        "Can not create existent booking status!!!"
      );
      return ResponseEntity.ok(bookingStat);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @PostMapping(
    value = "/setBookingListStatus",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> setBookingListStatus(
    @RequestBody List<BookingStatusRequest> bookingStatusList
  ) {
    try {
      BookingStatus bookingStat = bookingStatusService.setBookingStatus(
        bookingStatusList
      );
      if (bookingStat == null) return ResponseEntity.ok(
        "Can not create existent booking status!!!"
      );
      return ResponseEntity.ok(bookingStat);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }

  @GetMapping(
    value = "/getStatusBooking/{bookingStatus}",
    consumes = "application/json",
    produces = "application/json"
  )
  public ResponseEntity<?> getStatusBooking(
    @PathVariable String bookingStatus
  ) {
    try {
      BookingStatus bookingStat = bookingStatusService.getBookingStatus(
        bookingStatus
      );

      if (bookingStat == null) return ResponseEntity.ok(
        "Can not get booking status!!!"
      );
      return ResponseEntity.ok(bookingStat);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e);
    }
  }
}
