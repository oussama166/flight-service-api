package org.jetblue.jetblue.Service.Implementation;

import org.jetblue.jetblue.Mapper.Booking.BookingInternal;
import org.jetblue.jetblue.Mapper.Booking.BookingMapper;
import org.jetblue.jetblue.Mapper.Booking.BookingResponse;
import org.jetblue.jetblue.Models.DAO.*;
import org.jetblue.jetblue.Models.DTO.SeatPassengerDTO;
import org.jetblue.jetblue.Repositories.*;
import org.jetblue.jetblue.Service.BookingService;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookingImpl implements BookingService {

    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;
    private final FlightRepo flightRepo;
    private final BookingStatusRepo bookingStatusRepo;
    private final SeatsRepo seatsRepo;

    public BookingImpl(BookingRepo bookingRepo, UserRepo userRepo, FlightRepo flightRepo, BookingStatusRepo bookingStatusRepo, SeatsRepo seatsRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.flightRepo = flightRepo;
        this.bookingStatusRepo = bookingStatusRepo;
        this.seatsRepo = seatsRepo;
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BookingImpl.class);
    // TODO : Need to implement a booking stack priority for booking logic
    // !ANCHOR : Data need to be appreciated here

    @Override
    public BookingResponse setBooking(String username, long flight_number, String seat_label) {
        User user = findUserByUsername(username);
        Seat seat = gettingSeat(flight_number, seat_label);
        Flight flight = seat.getFlight();
        BookingStatus stats = bookingStatusRepo.findByStatus("Confirmed").orElse(null);

        if (!flight.getStatus().getStatus().equalsIgnoreCase("On Time")) {
            throw new IllegalArgumentException("Flight is not available for booking");
        }
        verifyUser(user);
        checkSeatAvailability(seat, flight);
        ensureSeatIsNotReserved(seat);

        Booking book = Booking.builder()
                .seats(seat)
                .totalPrice(seat.getPrice())
                .flight(flight)
                .user(user)
                .status(stats)
                .build();


        System.out.println("Booking created successfully for user: " + user.getUsername() + " on flight: " + flight.getFlightNumber());
        bookingRepo.save(book);
        seat.setAvailable(false);
        seatsRepo.save(seat);


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

    @Override
    public Booking setBooking(String username, Set<SeatPassengerDTO> passengerDTOSet) {
//        // association rule for multi seat
//
//        User user = findUserByUsername(username);
//        Flight flight = findFlightBySeat(passengerDTOSet.iterator().next().getSeatNumber());
//
//        verifyUser(user);
//
//        // check if the user had max or min required
//        if (passengerDTOSet.size() > 9) {
//            throw new IllegalArgumentException("You can not add more then 9 passengers in one flight reservation");
//        }
//
//
//        passengerDTOSet.forEach(seat -> {
//
//            Passenger passenger = Passenger.builder()
//                    .firstName(seat.getFirstName())
//                    .lastName(seat.getLastName())
//                    .middleName(seat.getMiddleName())
//                    .age(seat.getAge())
//                    .isUnaccompanied(seat.isUnaccompanied())
//                    .documents(seat.getDocuments())
//                    .user(user)
//                    .build();
//            double passengerPrice = 0.0;
//            Seat SeatInfo = gettingSeat(seat.getSeatNumber());
//
//
//            String bookingAllow = BookingUtils.validatePassenger(passenger, flight);
//
//            if (bookingAllow != null) {
//                new Exception(bookingAllow);
//            }
//
//            checkSeatAvailability(SeatInfo, flight);
//            ensureSeatIsNotReserved(SeatInfo);
//
//            // Add new passengers to user
//            user.setPassengers(List.of(passenger));
//            userRepo.save(user);
//
//            // create new booking
//            passengerPrice = BookingUtils.calculateTicketPrice(passenger,flight);
//
//            bookingRepo.save(
//                    Booking.builder()
//                            .user(user)
//                            .flight(flight)
//                            .totalPrice(passengerPrice)
//                            .build()
//
//            );
//
//
//
//
//        });
//
//
//    }
        return new Booking();
    }
    // !ANCHOR : Data need to be appreciated here

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


    // Helper function

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

    private void verifyUser(User user) {
        if (!user.isVerified()) {
            throw new IllegalArgumentException("User is not verified");
        }
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
