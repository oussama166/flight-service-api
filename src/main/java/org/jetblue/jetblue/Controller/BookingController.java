package org.jetblue.jetblue.Controller;

import jakarta.websocket.server.PathParam;
import org.jetblue.jetblue.Models.DAO.Booking;
import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;
import org.jetblue.jetblue.Service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class BookingController {

    // Inject the imp
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(
            value = "setBooking/{username}/{seatNumber}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setBooking(@PathVariable String seatNumber, @PathVariable String username) {
        System.out.println(
                "username" + username + "seatNumber" + seatNumber
        );
        Booking bookingRes = bookingService.setBooking(username,Long.parseLong(seatNumber));

        if(bookingRes == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookingRes);
    }

    @PostMapping(
            value ="setBookingList/{username}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> setBookingList(@PathParam("username") String userName, @RequestBody Set<SeatPassengerDTO> passengersSeats) {
        Booking bookingRes = bookingService.setBooking(userName,passengersSeats);
        if(bookingRes == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookingRes);
    }
    @GetMapping(
            value="getUserBooking/{username}",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<?> getUserBooking(@PathVariable String username) {
        List<Booking> bookingList = bookingService.getUserBookings(username);
        return ResponseEntity.ok(bookingList);
    }

}
