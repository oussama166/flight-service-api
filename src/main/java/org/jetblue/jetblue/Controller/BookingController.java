package org.jetblue.jetblue.Controller;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetblue.jetblue.Mapper.Booking.BookingFeeResponse;
import org.jetblue.jetblue.Mapper.Booking.BookingRequest;
import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;
import org.jetblue.jetblue.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class BookingController {

    // Inject the imp
    private final BookingService bookingService;

    @PostMapping(
            value = "setBooking/",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setBooking(
            @RequestBody BookingRequest booking
    ) {
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
    public ResponseEntity<?> setBookingList(@PathVariable(value = "username") String userName, @RequestBody Set<SeatPassengerDTO> passengersSeats) {
        BookingFeeResponse bookingRes = bookingService.setBookings(userName, passengersSeats);
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
        List<BookingResponse> bookingList = bookingService.getUserBookings(username);
        return ResponseEntity.ok(bookingList);
    }

}
