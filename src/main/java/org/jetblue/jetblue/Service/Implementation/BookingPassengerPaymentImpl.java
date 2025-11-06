package org.jetblue.jetblue.Service.Implementation;

import static com.stripe.Stripe.partnerId;
import static org.jetblue.jetblue.Utils.UserUtils.validateUser;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Models.DAO.BookingPassengerPayment;
import org.jetblue.jetblue.Models.DAO.Payment;
import org.jetblue.jetblue.Models.ENUM.PaymentGateway;
import org.jetblue.jetblue.Models.ENUM.PaymentMethod;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;
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
  @Transactional
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
    var user = userOpt.orElseThrow(
      () -> new IllegalArgumentException("User not found: " + username)
    );

    var bookingOpt = bookingRepo.findByBookingId(UUID.fromString(bookingId));
    var booking = bookingOpt.orElseThrow(
      () -> new IllegalArgumentException("Booking not found: " + bookingId)
    );

    var passengerOpt = passengerBookingRepo.findById(passengerId);
    var passenger = passengerOpt
      .orElseThrow(
        () ->
          new IllegalArgumentException("Passenger not found: " + passengerId)
      )
      .getPassenger();

    var flight = booking.getFlight();
    log.warn("BookingId: {}, Flight: {}", bookingId, flight);

    Optional<Payment> existingRecord = paymentRepo.findOne(
      (root, query, builder) ->
        builder.equal(root.get("booking").get("bookingId"), bookingId)
    );

    if (existingRecord.isPresent()) {
      log.info(
        "Updating total amount for BookingPassengerPayment record for user: {}, bookingId: {}, passengerId: {} and flight Number is {} with the amount of {} dollar",
        username,
        bookingId,
        passengerId,
        flight.getFlightNumber(),
        amount
      );

      booking.setTotalPrice(booking.getTotalPrice() + amount);

      var paymentLinked = Payment
        .builder()
        .amount(String.valueOf(amount))
        .currency("USD")
        .method(PaymentMethod.CREDIT_CARD)
        .paymentGateway(PaymentGateway.STRIPE)
        .status(PaymentStatus.PENDING)
        .creditCard(user.getCreditCards().get(0))
        .build();

      Payment parentPayment = existingRecord.get().getBooking().getPayment();
      if (parentPayment == null) {
        throw new IllegalStateException("Booking has no parent payment");
      }
      paymentLinked.setLinkedPayment(parentPayment);

      paymentRepo.save(paymentLinked);
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
