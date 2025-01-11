package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.ENUM.SeatType;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Repositories.SeatsRepo;
import org.jetblue.jetblue.Service.SeatService;
import org.jetblue.jetblue.Utils.SeatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class SeatImpl implements SeatService {

    // Injection
    private final SeatsRepo seatsRepo;
    private final FlightRepo flightRepo;

    private static final Logger LOG = LoggerFactory.getLogger(SeatImpl.class);

    // Implementation


    // Todo : Refactoring the create Seats logic to adapt the new approach column x row
    @Override
    public List<Seat> createSeats(int maxSeatNumber, double price, SeatType seatType, String airplaneName, int startRow) {
        List<Seat> seats = new ArrayList<>();

        // finding the flight using the flight number
        Flight flight = flightRepo.findByFlightNumber(airplaneName).orElseThrow(
                () -> new DataIntegrityViolationException("Flight " + airplaneName + " not found")
        );

        int col = flight.getAirline().getColFormation();
        int row = flight.getAirline().getRowFormation();
        int counter = 0;

        for (int i = 1; i <= row; i++) {
            if (counter < maxSeatNumber) {
                for (int j = 1; j <= col; j++) {
                    String seatLabel = SeatUtils.generateSingleSeat(i, j);
                    boolean isExist = seatsRepo.existsByFlightFlightNumberAndSeatLabel(flight.getFlightNumber(), seatLabel);
                    if (isExist) {
                        continue;
                    }
                    seats.add(createSeat(
                            SeatCreate.builder()
                                    .flag("")
                                    .price(price)
                                    .seatType(seatType)
                                    .flightNumber(flight.getFlightNumber())
                                    .col(i)
                                    .row(j)
                                    .build()
                    ));
                    counter++;
                }
            }
        }


        // Loop through and create seats up to the max seat number


        // seatsRepo.saveAll(seats);
        return seats;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Seat createSeat(SeatCreate seat) {

        // Fetch the flight associated with the seat
        Flight flight = seatsRepo.findByFlightFlightNumber(seat.getFlightNumber()).orElseThrow(
                () -> new DataIntegrityViolationException("Flight associated with seat not found")
        );

        if (flight.getStatus().getStatus().contentEquals("Revoked") || flight.getStatus().getStatus().contentEquals("canceled")) {
            throw new DataIntegrityViolationException("We can not have seat on revoked or canceled flight");
        }

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
        seatInst.setSeatLabel(SeatUtils.generateSingleSeat(seat.getCol(), seat.getRow()));
        seatInst.setFlight(flight);
        seatInst.setAvailable(true);
        seatInst.setLeapEnfantSeat(seat.isLapEnfant());
        seatInst.setSpecialTrait(seat.isSpecialTrait());
        seatInst.setCol(seat.getCol());
        seatInst.setRow(seat.getRow());
        seatInst.setSold(seat.isSold());


        // Save the seat instance and return
        return seatsRepo.save(seatInst);
    }

    @Override
    public Seat updateSeat(int seatId, Seat seatInfo, String airplaneName) throws ExecutionException {
        // trying to find first the flight
        Seat seat = seatsRepo
                .findBySeatNumberAndFlight(seatId, airplaneName)
                .orElseThrow(() -> new DataIntegrityViolationException("Seat not found"));
        LOG.info(seat.toString());
        try {
            seat.setPrice(seatInfo.getPrice());
            seat.setAvailable(seat.isAvailable());
            seat.setSeatType(seatInfo.getSeatType());
            seat.setFlight(seatInfo.getFlight());
            seat.setPrice(seatInfo.getPrice());
            seat.setAvailable(seatInfo.isAvailable());
            seat.setLeapEnfantSeat(seatInfo.isLeapEnfantSeat());
            seat.setSpecialTrait(seatInfo.isSpecialTrait());
            seat.setSold(seatInfo.isSold());

            return seatInfo;
        } catch (Exception e) {
            throw new ExecutionException(e.getMessage(), e);
        }
    }

    @Override
    public Seat getSeat(int seatId) {
        return seatsRepo.findBySeatNumber(seatId).orElse(null);
    }

    @Override
    public List<Seat> getAllSeats(String flightNumber) {

        return seatsRepo.findByFlight_FlightNumber(flightNumber);
    }
}
