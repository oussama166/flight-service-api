package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.ENUM.SeatType;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Repositories.SeatsRepo;
import org.jetblue.jetblue.Service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SeatImpl implements SeatService {

    // Injection
    private final SeatsRepo seatsRepo;
    private final FlightRepo flightRepo;

    private static final Logger LOG = LoggerFactory.getLogger(SeatImpl.class);

    // Implementation


    @Override
    public List<Seat> createSeats(int maxSeatNumber, double price, SeatType seatType, String airplaneName) {
        List<Seat> seats = new ArrayList<Seat>();

        // finding the flight using the flight number
        Flight flight = flightRepo.findByFlightNumber(airplaneName).orElse(null);

        if (flight == null) {
            return null;
        }
        LOG.info(flight.toString());
        // Loop through and create seats up to the max seat number
        for (int i = 1; i <= maxSeatNumber; i++) {
            // creating seat depend on the existing transactional function
            seats
                    .add(
                            createSeat(
                                    SeatCreate
                                            .builder()
                                            .price(price)
                                            .seatType(seatType)
                                            .flightNumber(flight.getFlightNumber())
                                            .flag("")
                                            .build()));
        }

        seatsRepo.saveAll(seats);
        return seats;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Seat createSeat(SeatCreate seat) {

        // Fetch the flight associated with the seat
        Flight flight = seatsRepo.findByFlightFlightNumber(seat.getFlightNumber()).orElseThrow(
                () -> new DataIntegrityViolationException("Flight associated with seat not found")
        );

        // Check the seat type and update reservation counts accordingly
        switch (seat.getSeatType()) {
            case FIRST_CLASS:
                if (flight.getFirstClassReserve() < flight.getMaxFirstClass()) {
                    flight.setFirstClassReserve(flight.getFirstClassReserve() + 1);
                } else {
                    throw new DataIntegrityViolationException("First class reserve is full");
                }
                break;

            case SECOND_CLASS:
                if (flight.getSecondClassReserve() < flight.getMaxSecondClass()) {
                    flight.setSecondClassReserve(flight.getSecondClassReserve() + 1);
                } else {
                    throw new DataIntegrityViolationException("Second class reserve is full");
                }
                break;

            case ECONOMY_CLASS:
                if (flight.getThirdClassReserve() < flight.getMaxThirdClass()) {
                    flight.setThirdClassReserve(flight.getThirdClassReserve() + 1);
                } else {
                    throw new DataIntegrityViolationException("Economy class reserve is full");
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid seat type");
        }

        // Create the seat and set its properties
        Seat seatInst = new Seat();
        seatInst.setPrice(seat.getPrice());
        seatInst.setSeatType(seat.getSeatType());
        seatInst.setFlag(seat.getFlag());
        seatInst.setFlight(flight);
        seatInst.setAvailable(true);

        // Save the seat instance and return
        return seatsRepo.save(seatInst);
    }

    @Override
    public Seat updateSeat(int seatId, Seat seatInfo, String airplaneName) {
        // trying to find first the flight
        Seat seat = seatsRepo.findBySeatNumberAndFlight(seatId, airplaneName).orElse(null);

        if (seat == null) {
            return null;
        }

        seat.setPrice(seatInfo.getPrice());
        seat.setAvailable(seat.isAvailable());
        seat.setSeatType(seatInfo.getSeatType());

        return seatInfo;

    }

    @Override
    public Seat getSeat(int seatId) {
        return seatsRepo.findBySeatNumber(seatId).orElse(null);
    }


}
