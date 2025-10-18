package org.jetblue.jetblue.Service.Implementation;

import static org.jetblue.jetblue.Utils.UserUtils.validateUser;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.jetblue.jetblue.Repositories.BookingPassengerPaymentRepo;
import org.jetblue.jetblue.Repositories.BookingPassengerRepo;
import org.jetblue.jetblue.Repositories.BookingRepo;
import org.jetblue.jetblue.Repositories.PassengerRepo;
import org.jetblue.jetblue.Repositories.PaymentRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.BookingPassengerPaymentService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class BookingPassengerPaymentImpl
  implements BookingPassengerPaymentService {
  private BookingPassengerPaymentRepo bookingPassengerPaymentRepo;
  private BookingPassengerRepo bookingPassengerRepo;
  private UserRepo userRepo;
  private BookingRepo bookingRepo;
  private PassengerRepo passengerRepo;
  private BookingPassengerRepo passengerBookingRepo;
  private PaymentRepo paymentRepo;

  @Override
  public void addBookingPassengerPayment(
    String username,
    String bookingId,
    Long passengerId,
    String paymentId
  ) {
    validateUser(username);
    var user = userRepo.findByUsername(username).get();
    var booking = bookingRepo.findByBookingId(UUID.fromString(bookingId)).get();
    var passenger = bookingPassengerRepo.findById(passengerId).get();
    var payment = paymentRepo.findById(paymentId).get();
    var flight = booking.getFlight();

    var existingRecord = bookingPassengerPaymentRepo.findByFlight_FlightNumberAndUser_IdAndPassenger_Id(
      flight.getFlightNumber(),
      user.getId(),
      passenger.getId()
    );
    log.warn(existingRecord.toString());
    if (existingRecord.isEmpty()) {
      log.info(
        "Creating new BookingPassengerPayment record for user: {}, bookingId: {}, passengerId: {}, paymentId: {} and flight Number is {}",
        username,
        bookingId,
        passengerId,
        paymentId,
        flight.getFlightNumber()
      );
      var bookingPassengerPayment = BookingPassengerPayment
        .builder()
        .passenger(passenger.getPassenger())
        .payment(payment)
        .user(user)
        .flight(flight)
        .build();
      bookingPassengerPaymentRepo.save(bookingPassengerPayment);
    }
  }

  @Override
  public void updateBookingPassengerPaymentTotalAmount(
    String username,
    String bookingId,
    Long passengerId,
    double amount
  ) {
    validateUser(username);
    log.debug(
      "username : " +
      username +
      " BookingId : " +
      bookingId +
      " passengerId : " +
      passengerId +
      " amount : " +
      amount
    );

    var userOpt = userRepo.findByUsername(username);
    if (userOpt.isEmpty()) {
      log.warn("User not found for username: {}", username);
      throw new IllegalArgumentException("User not found: " + username);
    }
    var user = userOpt.get();

    var bookingOpt = bookingRepo.findByBookingId(UUID.fromString(bookingId));
    if (bookingOpt.isEmpty()) {
      log.warn("Booking not found for bookingId: {}", bookingId);
      throw new IllegalArgumentException("Booking not found: " + bookingId);
    }
    var booking = bookingOpt.get();

    var passengerOpt = passengerBookingRepo.findById(passengerId);
    if (passengerOpt.isEmpty()) {
      log.warn("Passenger not found for passengerId: {}", passengerId);
      throw new IllegalArgumentException("Passenger not found: " + passengerId);
    }
    var passenger = passengerOpt.get().getPassenger();

    var flight = booking.getFlight();

    log.warn("BookingId: {}, Flight: {}", bookingId, flight);

    var existingRecord = bookingPassengerPaymentRepo.findByFlight_FlightNumberAndUser_IdAndPassenger_Id(
      flight.getFlightNumber(),
      user.getId(),
      passenger.getId()
    );
    if (existingRecord.isPresent()) {
      log.info(
        "Updating total amount for BookingPassengerPayment record for user: {}, bookingId: {}, passengerId: {} and flight Number is {}",
        username,
        bookingId,
        passengerId,
        flight.getFlightNumber()
      );
      booking.setTotalPrice(amount);
      var bookingPassengerPayment = existingRecord.get();
      bookingPassengerPayment.getPayment().setAmount(String.valueOf(amount));
      bookingPassengerPaymentRepo.save(bookingPassengerPayment);
    } else {
      log.warn(
        "No existing BookingPassengerPayment record found for user: {}, bookingId: {}, passengerId: {} and flight Number is {}. Cannot update total amount.",
        username,
        bookingId,
        passengerId,
        flight.getFlightNumber()
      );
    }
  }
}
