package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Seat.SeatMapper;
import org.jetblue.jetblue.Mapper.Seat.SeatResponse;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.ENUM.SeatType;
import org.jetblue.jetblue.Repositories.AirplaneRepo;
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
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class SeatImpl implements SeatService {

    // Injection
    private final SeatsRepo seatsRepo;
    private final FlightRepo flightRepo;

    private static final Logger LOG = LoggerFactory.getLogger(SeatImpl.class);
    private final AirplaneRepo airplaneRepo;

    // Implementation



    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SeatResponse> createSeats(int maxSeatNumber, double price, SeatType seatType, String airplaneName, int startRow) {
        List<Seat> seats = new ArrayList<>();

        // Find the flight associated with the airplane name
        Flight flight = flightRepo.findByFlightNumber(airplaneName).orElseThrow(
                () -> new DataIntegrityViolationException("Flight " + airplaneName + " not found")
        );

        // Validate flight status
        if (flight.getStatus().getStatus().equals("Revoked") || flight.getStatus().getStatus().equals("Canceled")) {
            throw new DataIntegrityViolationException("Cannot create seats for a revoked or canceled flight");
        }

        // Get airplane information
        int col = flight.getAirline().getColFormation();
        int row = flight.getAirline().getRowFormation();
        int counter = 0;

        for (int i = startRow; i <= row; i++) {
            if (counter >= maxSeatNumber) {
                break;
            }
            for (int j = 1; j <= col; j++) {
                if (counter >= maxSeatNumber) {
                    break;
                }

                // Generate seat label
                String seatLabel = SeatUtils.generateSingleSeat(i, j);

                // Check if seat already exists
                boolean isExist = seatsRepo.existsByFlightFlightNumberAndSeatLabel(flight.getFlightNumber(), seatLabel);
                if (isExist) {
                    continue;
                }

                // Update reservation counts based on seat type
                switch (seatType) {
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

                // Create and configure the seat
                Seat seat = new Seat();
                seat.setFlight(flight);
                seat.setSeatType(seatType);
                seat.setSeatLabel(seatLabel);
                seat.setRow(i);
                seat.setCol(j);
                seat.setPrice(price);
                seat.setAvailable(true);
                seat.setSold(false);
                seat.setSpecialTrait(false);
                seat.setFlag("");
                seat.setAirplane(flight.getAirplane());
                seats.add(seat);

                counter++;
            }
        }

        // Save all seats in bulk
        List<Seat> sta = seatsRepo.saveAll(seats);
        return sta.stream().map(SeatMapper::toSeatResponse).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SeatResponse createSeat(SeatCreate seat) {

        // Fetch the flight associated with the seat
        Flight flight = seatsRepo.findByFlightFlightNumber(seat.getFlightNumber()).orElseThrow(
                () -> new DataIntegrityViolationException("Flight associated with seat not found")
        );

        Optional<Airplane> airplane = airplaneRepo.findByName(flight.getAirplane().getName());


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
        Seat seatInst = getSeat(seat, flight, airplane.get());


        // Save the seat instance and return
        Seat st= seatsRepo.save(seatInst);
        return SeatMapper.toSeatResponse(st);
    }


    @Override
    public Seat updateSeat(String seatLabel, Seat seatInfo, String airplaneName) throws ExecutionException {
        // trying to find first the flight
        Seat seat = seatsRepo
                .findBySeatNumberAndFlight(seatLabel, airplaneName)
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
    public Seat getSeat(String seatLabel) {
        return seatsRepo.findBySeatLabel(seatLabel).orElse(null);
    }

    @Override
    public List<Seat> getAllSeats(String flightNumber) {

        return seatsRepo.findByFlight_FlightNumber(flightNumber);
    }


    // Helper functions

    private static Seat getSeat(SeatCreate seat, Flight flight, Airplane airplane) {
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
        seatInst.setAirplane(airplane);
        return seatInst;
    }

}
