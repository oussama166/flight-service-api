package org.jetblue.jetblue.Service.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetblue.jetblue.Models.DAO.BookingBaggageFlight;
import org.jetblue.jetblue.Repositories.BookingBaggageFlightRepo;
import org.jetblue.jetblue.Repositories.BookingPassengerRepo;
import org.jetblue.jetblue.Repositories.FlightBaggageOfferRepo;
import org.jetblue.jetblue.Service.BookingPassengerPaymentService;
import org.jetblue.jetblue.Service.FlightBaggageBookingService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FlightBaggageBookingImpl implements FlightBaggageBookingService {
  private final BookingPassengerRepo bookingPassenger;
  private final FlightBaggageOfferRepo flightBaggageOfferRepo;
  private final BookingBaggageFlightRepo bookingBaggageFlightRepo;

  private final BookingPassengerPaymentImpl bookingPassengerService;

  @Override
  @Transactional
  public void bookBaggageForFlight(int passengerId, String flightBaggageRef) {
    var passenger = bookingPassenger
      .findOne(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.equal(root.get("id"), passengerId)
      )
      .orElseThrow(() -> new RuntimeException("Passenger not found"));
    var baggageOffer = flightBaggageOfferRepo
      .findOne(
        (root, query, criteriaBuilder) ->
          criteriaBuilder.and(
            criteriaBuilder.equal(root.get("codeOffer"), flightBaggageRef),
            criteriaBuilder.equal(
              root.get("flight").get("id"),
              passenger.getSeat().getFlight().getId()
            )
          )
      )
      .orElseThrow(() -> new RuntimeException("Baggage offer not found"));

    bookingBaggageFlightRepo.save(
      BookingBaggageFlight
        .builder()
        .bookingPassenger(passenger)
        .flightBaggageOffer(baggageOffer)
        .quantity(1)
        .priceAtPurchase(baggageOffer.getPrice())
        .build()
    );

    log.info(
      "Booking baggage: user='{}', bookingId='{}', passengerId={}, previousTotal={}, baggagePrice={}",
      passenger.getPassenger().getUser().getUsername(),
      passenger.getBooking().getBookingId(),
      passengerId,
      passenger.getBooking().getTotalPrice(),
      baggageOffer.getPrice()
    );

    String username = passenger.getPassenger().getUser().getUsername();
    String bookingId = passenger.getBooking().getBookingId().toString();
    Long passengerIdLong = Long.valueOf(passengerId);
    double updatedTotal =
      passenger.getBooking().getTotalPrice() + baggageOffer.getPrice();

    bookingPassengerService.updateBookingPassengerPaymentTotalAmount(
      username,
      bookingId,
      passengerIdLong,
      updatedTotal
    );
  }
}
