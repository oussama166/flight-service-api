package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.jetblue.jetblue.Repositories.*;
import org.jetblue.jetblue.Service.BookingPassengerPaymentService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.jetblue.jetblue.Utils.UserUtils.validateUser;

@Slf4j
@Service
@AllArgsConstructor
public class BookingPassengerPaymentImpl implements BookingPassengerPaymentService {

    private BookingPassengerPaymentRepo bookingPassengerPaymentRepo;
    private UserRepo userRepo;
    private BookingRepo bookingRepo;
    private PassengerRepo passengerRepo;
    private PaymentRepo paymentRepo;


    @Override
    public void addBookingPassengerPayment(String username, String bookingId, Long passengerId, String paymentId) {
        validateUser(username);
        var user = userRepo.findByUsername(username).get();
        var booking = bookingRepo.findByBookingId(UUID.fromString(bookingId)).get();
        var passenger = passengerRepo.findById(passengerId).get();
        var payment = paymentRepo.findById(paymentId).get();
        var flight = booking.getFlight();

        var existingRecord = bookingPassengerPaymentRepo.findByFlight_FlightNumberAndUser_IdAndPassenger_Id(
                flight.getFlightNumber(), user.getId(), passenger.getId()
        );
        log.warn(existingRecord.toString());
        if (existingRecord.isEmpty()) {
            log.info("Creating new BookingPassengerPayment record for user: {}, bookingId: {}, passengerId: {}, paymentId: {} and flight Number is {}", username, bookingId, passengerId, paymentId, flight.getFlightNumber());
            var bookingPassengerPayment = BookingPassengerPayment.builder()
                    .passenger(passenger)
                    .payment(payment)
                    .user(user)
                    .flight(flight)
                    .build();
            bookingPassengerPaymentRepo.save(bookingPassengerPayment);
        }


    }
}
