package org.jetblue.jetblue.Service.Implementation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Events.SeatCancelledEvent;
import org.jetblue.jetblue.Events.SeatDynamizedPriceEvent;
import org.jetblue.jetblue.Events.SeatReservedEvent;
import org.jetblue.jetblue.Mapper.Booking.BookingFeeResponse;
import org.jetblue.jetblue.Mapper.Booking.BookingInternal;
import org.jetblue.jetblue.Mapper.Booking.BookingMapper;
import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;
import org.jetblue.jetblue.Models.ENUM.PaymentGateway;
import org.jetblue.jetblue.Models.ENUM.PaymentMethod;
import org.jetblue.jetblue.Models.ENUM.PaymentStatus;
import org.jetblue.jetblue.Models.ENUM.SeatType;
import org.jetblue.jetblue.Repositories.*;
import org.jetblue.jetblue.Service.BookingPassengerPaymentService;
import org.jetblue.jetblue.Service.BookingService;
import org.jetblue.jetblue.Utils.BookingUtils;
import org.jetblue.jetblue.Utils.PriceEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static org.jetblue.jetblue.Utils.BookingUtils.calculateTicketPrice;
import static org.jetblue.jetblue.Utils.DocumentUtils.createPassengerFolderFromPath;
import static org.jetblue.jetblue.Utils.PathEncoded.EncodeFilePath;
import static org.jetblue.jetblue.Utils.PriceEngine.discountedPricing;
import static org.jetblue.jetblue.Utils.PriceEngine.dynamicPricing;
import static org.jetblue.jetblue.Utils.UserUtils.*;

@Service
@AllArgsConstructor
public class BookingImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final FlightRepo flightRepo;
    private final BookingStatusRepo bookingStatusRepo;
    private final SeatsRepo seatsRepo;
    private final BookingPassengerRepo bookingPassengerRepo;
    private final PassengerRepo passengerRepo;
    private final DocumentRepo documentRepo;
    private final PaymentRepo paymentRepo;

    // Publisher for events
    private final ApplicationEventPublisher publisher;

    // Service
    private BookingPassengerPaymentService bookingPassengerPaymentService;


    private static final Logger logger = LoggerFactory.getLogger(BookingImpl.class);
    private final BookingPassengerPaymentRepo bookingPassengerPaymentRepo;
    // TODO : Need to implement a booking stack priority for booking logic
    // !ANCHOR : Data need to be appreciated here

    @Override
    @Transactional
    public BookingResponse setBooking(String username, long flight_number, String seat_label) {
        validateUser(username);

        logger.info("Booking request for user: {}", username);

        User user = findUserByUsername(username);
        Seat seat = gettingSeat(flight_number, seat_label);
        Flight flight = seat.getFlight();
        BookingStatus stats = bookingStatusRepo.findByStatus("Pending")
                .orElseThrow(() -> new IllegalStateException("Pending booking status not found"));

        if (seat == null || flight == null) {
            throw new DataAccessResourceFailureException("Seat not found for flight number: " + flight_number + " and seat label: " + seat_label);
        }
        if (!"On Time".equalsIgnoreCase(flight.getStatus().getStatus())) {
            throw new IllegalArgumentException("Flight is not available for booking");
        }

        verifyUser(user);
        checkSeatAvailability(seat, flight);
        ensureSeatIsNotReserved(seat);
        boolean verifyUserDocuments = verifyUserDocuments(passengerRepo.findPassengerByUser_UsernameAndFirstNameAndLastNameAndBirthDate(
                        user.getUsername(),
                        user.getName(),
                        user.getLastName(),
                        user.getBirthday()
                ).get()
        );

        if (!verifyUserDocuments) {
            throw new IllegalArgumentException("User documents are not verified");
        }


        Booking book = Booking.builder()
                .seat(seat)
                .totalPrice(seat.getPrice())
                .flight(flight)
                .user(user)
                .status(stats)
                .build();

        bookingRepo.save(book);
        seat.setAvailable(false);
        seatsRepo.save(seat);


        // ATTENTION : Publish the event after booking is saved
        publisher.publishEvent(new SeatReservedEvent(this, flight.getId(), seat.getSeatType().toSeatTypeName()));
        publisher.publishEvent(new SeatDynamizedPriceEvent(this, flight));

        passengerRepo.findPassengerByUser_UsernameAndFirstNameAndLastNameAndBirthDate(
                user.getUsername(),
                user.getName(),
                user.getLastName(),
                user.getBirthday()
        ).ifPresentOrElse(
                passenger -> {
                    BookingPassenger bookingPassenger = BookingPassenger.builder()
                            .booking(book)
                            .passenger(passenger)
                            .seat(seat)
                            .build();
                    bookingPassengerRepo.save(bookingPassenger);
                },
                () -> {
                    throw new DataIntegrityViolationException("Passenger not found for user: " + user.getUsername());
                }
        );

        Payment payment = Payment.builder()
                .booking(book)
                .amount(Double.toString(book.getTotalPrice()))
                .currency("USD")
                .method(PaymentMethod.CREDIT_CARD)
                .paymentGateway(PaymentGateway.STRIPE)
                .status(PaymentStatus.PENDING)
                .creditCard(user.getCreditCards().get(0))
                .build();
    
        paymentRepo.save(payment);

        logger.info("Booking created successfully for user: {} on flight: {}", user.getUsername(), flight.getFlightNumber());

        return BookingMapper.toBookingResponse(
                new BookingInternal(
                        user.getUsername(),
                        flight.getFlightNumber(),
                        seat.getSeatLabel(),
                        stats.getStatus(),
                        book.getTotalPrice(),
                        false
                )
        );
    }

    @Transactional
    @Override
    public BookingFeeResponse setBookings(String username, Set<SeatPassengerDTO> passengerDTOSet, String flightNumber) {
        validateUser(username);
        validatePassengerSet(passengerDTOSet);
        validateBookingUser(username, flightNumber);
        validateFlightReservation(passengerDTOSet);

        User user = findUserByUsername(username);
        verifyUser(user);

        double totalPrice = 0.0;
        List<String> seatsLabels = new ArrayList<>();
        Flight bookedFlight = null;

        for (SeatPassengerDTO seatDTO : passengerDTOSet) {
            Seat seatInfo = seatsRepo.findById(seatDTO.getSeatNumber())
                    .orElseThrow(() -> new DataAccessResourceFailureException(
                            "Seat not found for seat number: " + seatDTO.getSeatNumber()
                    ));

            Flight flight = findFlightBySeat(seatInfo);
            if (flight == null) {
                throw new DataAccessResourceFailureException(
                        "Flight not found for seat number: " + seatDTO.getSeatNumber()
                );
            }

            // Ensure same flight
            if (bookedFlight == null) {
                bookedFlight = flight;
            } else if (!(bookedFlight.getId() == flight.getId())) {
                throw new IllegalArgumentException("All seats must belong to the same flight");
            }

            Passenger passenger = buildPassenger(seatDTO, user);
            Seat availableSeat = gettingSeat(flight.getId(), seatInfo.getSeatLabel());

            String validationMsg = BookingUtils.validatePassenger(passenger, flight);
            if (validationMsg != null) {
                throw new IllegalArgumentException(validationMsg);
            }

            checkSeatAvailability(availableSeat, flight);
            ensureSeatIsNotReserved(availableSeat);

            Passenger existingPassenger = passengerRepo
                    .findPassengerByUser_UsernameAndFirstNameAndLastNameAndBirthDate(
                            user.getUsername(),
                            passenger.getFirstName(),
                            passenger.getLastName(),
                            passenger.getBirthDate()
                    )
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Passenger documents not found for: " + passenger.getFirstName() + " " + passenger.getLastName()
                    ));

            if (!verifyUserDocuments(existingPassenger)) {
                throw new IllegalArgumentException("User documents are not verified for passenger: " +
                                                   passenger.getFirstName() + " " + passenger.getLastName());
            }

            if (bookingPassengerRepo.existsByPassenger_IdAndSeat_Flight_Id(passenger.getId(), flight.getId())) {
                throw new IllegalArgumentException("Passenger already booked for flight " + flight.getFlightNumber());
            }

            availableSeat.setAvailable(false);
            seatsRepo.save(availableSeat);

            publisher.publishEvent(new SeatReservedEvent(this, flight.getId(), availableSeat.getSeatType().toSeatTypeName()));

            double passengerPrice = calculateTicketPrice(passenger, seatInfo);
            double discountedPricing = discountedPricing(seatInfo.getSeatType(), passengerPrice, passengerDTOSet.size());
            totalPrice += discountedPricing;

            Booking booking = Booking.builder()
                    .flight(flight)
                    .user(user)
                    .seat(availableSeat)
                    .status(bookingStatusRepo.findByStatus("Pending")
                            .orElseThrow(() -> new DataIntegrityViolationException("Pending booking status not found")))
                    .totalPrice(passengerPrice)
                    .build();

            bookingRepo.save(booking);

            // Ensure user has at least 1 credit card
            if (user.getCreditCards().isEmpty()) {
                throw new IllegalArgumentException("User has no registered credit card for payment");
            }

            Payment payment = Payment.builder()
                    .booking(booking)
                    .amount(Double.toString(discountedPricing))
                    .currency("USD")
                    .method(PaymentMethod.CREDIT_CARD)
                    .paymentGateway(PaymentGateway.STRIPE)
                    .status(PaymentStatus.PENDING)
                    .creditCard(user.getCreditCards().get(0))
                    .build();

            paymentRepo.save(payment);

            bookingPassengerPaymentService.addBookingPassengerPayment(
                    user.getUsername(),
                    booking.getBookingId().toString(),
                    passenger.getId(),
                    payment.getId()
            );

            bookingPassengerRepo.save(
                    BookingPassenger.builder()
                            .booking(booking)
                            .passenger(passenger)
                            .seat(availableSeat)
                            .build()
            );

            seatsLabels.add(seatInfo.getSeatLabel());
        }

        if (bookedFlight == null) {
            throw new IllegalStateException("No flight found for booking");
        }

        return BookingFeeResponse.builder()
                .TotalPrice(totalPrice)
                .flightNumber(bookedFlight.getFlightNumber())
                .seatLabel(seatsLabels)
                .build();
    }

    private void validateFlightReservation(Set<SeatPassengerDTO> passengerDTOSet) {
        Set<Long> flightIds = passengerDTOSet.stream()
                .map(dto -> seatsRepo.findById(dto.getSeatNumber())
                        .orElseThrow(() -> new DataAccessResourceFailureException(
                                "Seat not found for seat number: " + dto.getSeatNumber()
                        ))
                        .getFlight()  // assuming Seat entity has a getFlight() method
                        .getId()
                )
                .collect(Collectors.toSet());

        // If more than 1 flightId is present, seats belong to multiple flights
        if (flightIds.size() > 1) {
            throw new IllegalArgumentException("All seats must belong to the same flight");
        }

    }

    private void validateBookingUser(String username, String flightNumber) {
        // Check if there is any booking for this user in the given flight
        long bookingCount = bookingPassengerPaymentRepo
                .countByFlight_FlightNumberAndUser_Username(flightNumber, username);

        if (bookingCount > 9) {
            throw new IllegalArgumentException("User " + username + "cannot book more than 9 seats on this flight");
        }
    }


    private void validatePassengerSet(Set<SeatPassengerDTO> passengerDTOSet) {
        if (passengerDTOSet.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one passenger to book a flight");
        }
        if (passengerDTOSet.size() > 9) {
            throw new IllegalArgumentException("You cannot add more than 9 passengers in one flight reservation");
        }
    }

    private Passenger buildPassenger(SeatPassengerDTO dto, User user) {
        Optional<Passenger> passengerExist = passengerRepo.findPassengerByUser_UsernameAndFirstNameAndLastNameAndBirthDate(
                user.getUsername(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthDate()
        );
        if (passengerExist.isPresent()) {
            return passengerExist.get();
        }
        Passenger passenger = Passenger.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .age(dto.getAge())
                .birthDate(dto.getBirthDate())
                .passportNumber(dto.getPassportNumber())
                .passportExpiryDate(dto.getPassportExpirationDate())
                .user(user)
                .build();

        passengerRepo.save(passenger);

        Set<Document> documents = dto.getDocuments().stream()
                .map(doc -> {
                    try {
                        String passengerFolderFromPath = EncodeFilePath(createPassengerFolderFromPath(
                                dto.getFirstName() + "_" + dto.getLastName(),
                                doc.documentType().getDisplayName(),
                                doc.documentFilePathLocally()
                        ));

                        Document document = Document.builder()
                                .passenger(passenger)
                                .type(doc.documentType())
                                .path(passengerFolderFromPath)
                                .build();
                        documentRepo.save(document);

                        return document;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })
                .collect(Collectors.toSet());

        passenger.setDocuments(documents);

        passengerRepo.save(passenger);

        return passenger;
    }


    @Override
    public List<BookingResponse> getUserBookings(String username) {

        User us = userRepo.findByUsername(username).orElse(null);
        if (us == null) {
            return null;
        }
        return bookingRepo.findBookingsByUser_Username(us.getUsername())
                .stream()
                .map(booking -> BookingMapper.toBookingResponse(
                        new BookingInternal(
                                booking.getUser().getUsername(),
                                booking.getFlight().getFlightNumber(),
                                "A1",
                                booking.getStatus().getStatus(),
                                booking.getTotalPrice(),
                                false
                        )
                ))
                .toList();


    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepo.findAllBy();
    }

    @Override
    public boolean cancelBooking(UUID bookingId) {
        Optional<Booking> bookingByBookingId = bookingRepo.findBookingByBookingId(bookingId);
        if (bookingByBookingId.isEmpty()) {
            throw new DataIntegrityViolationException("Booking not found with ID: " + bookingId);
        }
        Booking booking = bookingByBookingId.get();
        Flight flight = booking.getFlight();
        Seat seat = booking.getSeat();
        if (flight == null || seat == null) {
            throw new DataIntegrityViolationException("Flight or seat associated with booking not found");
        }
        String bookingStatus = booking.getStatus().getStatus();
        System.out.println("Booking Status: " + bookingStatus);
        // Check if the booking is already canceled
        if ("Canceled".equalsIgnoreCase(bookingStatus)) {
            throw new IllegalStateException("Booking is already canceled");
        }
        // Update the booking status to "Canceled"
        BookingStatus canceledStatus = bookingStatusRepo.findByStatus("Cancelled")
                .orElseThrow(() -> new DataIntegrityViolationException("Canceled booking status not found"));
        booking.setStatus(canceledStatus);
        bookingRepo.save(booking);
        // Mark the seat as available again
        seat.setAvailable(true);
        seatsRepo.save(seat);

        ///  ATTENTION : PUBLISH THE EVENT AFTER CANCELLATION
        publisher.publishEvent(new SeatCancelledEvent(this, flight.getId(), seat.getSeatType().toSeatTypeName()));
        // Log the cancellation
        logger.info("Booking with ID: {} has been canceled successfully", bookingId);
        return true;

    }


    // DocumentUtils function

    private Seat ExtractSeat(long FlightNumber, String seatNumber) {
        return seatsRepo.findByFlight_IdAndSeatLabel(FlightNumber, seatNumber).orElse(null);
    }

    private User findUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new DataIntegrityViolationException("User not found to be associated with flight!"));
    }

    private Flight findFlightBySeat(Seat seat) {
        return flightRepo.findByFlightNumber(seat.getFlight().getFlightNumber())
                .orElseThrow(() -> new DataIntegrityViolationException("Flight booking cannot be found!"));
    }

    private Flight findFlightBySeat(String seatLabel) {
        return seatsRepo.findFlightBySeatLabel(seatLabel).orElseThrow(() -> new DataIntegrityViolationException("Flight booking cannot be found!"));
    }


    private void checkSeatAvailability(Seat seat, Flight flight) {
        switch (seat.getSeatType()) {
            case FIRST_CLASS -> {
                if (!flight.isFirstClassAvailable()) throw new RuntimeException("First class sold out");
            }
            case SECOND_CLASS -> {
                if (!flight.isSecondClassAvailable()) throw new RuntimeException("Second class sold out");
            }
            case ECONOMY_CLASS -> {
                if (!flight.isThirdClassAvailable()) throw new RuntimeException("Third class sold out");
            }
        }
    }

    private void ensureSeatIsNotReserved(Seat seat) {
        System.out.println(seat.isAvailable());
        if (!seat.isAvailable()) {
            throw new IllegalArgumentException("Seat is already reserved");
        }
    }

    private double totalPrice(Set<Seat> seats) {
        double price = 0.0;
        for (Seat seat : seats) {
            price += seat.getPrice();
        }
        return price;

    }

    private Seat gettingSeat(long FlightNumber, String seatLabel) {
        return seatsRepo.findFirstByFlight_IdAndSeatLabel(FlightNumber, seatLabel).orElseThrow(
                () -> new DataAccessResourceFailureException("Can not found the seat")
        );
    }


}
