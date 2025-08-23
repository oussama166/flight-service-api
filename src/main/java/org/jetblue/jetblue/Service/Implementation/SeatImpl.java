package org.jetblue.jetblue.Service.Implementation;


import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Seat.SeatMapper;
import org.jetblue.jetblue.Mapper.Seat.SeatResponse;
import org.jetblue.jetblue.Models.DAO.Airplane;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;
import org.jetblue.jetblue.Models.DTO.SeatCreationRequest;
import org.jetblue.jetblue.Models.ENUM.SeatType;
import org.jetblue.jetblue.Repositories.AirplaneRepo;
import org.jetblue.jetblue.Repositories.FlightRepo;
import org.jetblue.jetblue.Repositories.SeatsRepo;
import org.jetblue.jetblue.Service.SeatService;
import org.jetblue.jetblue.Utils.SeatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.jetblue.jetblue.Utils.PriceEngine.calculatePriceSeat;
import static org.jetblue.jetblue.Utils.SeatUtils.getCreatedSeatInfo;

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
    public List<SeatResponse> createSeats(SeatCreationRequest seatCreationRequest) {


        List<Seat> seats = new ArrayList<>();

        // Find the flight associated with the airplane name
        Flight flight = flightRepo.findByFlightNumber(seatCreationRequest.getAirplaneName()).orElseThrow(
                () -> new DataIntegrityViolationException("Flight " + seatCreationRequest.getAirplaneName() + " not found")
        );

        // Validate flight status
        if (flight.getStatus().getStatus().equals("Revoked") || flight.getStatus().getStatus().equals("Canceled")) {
            throw new DataIntegrityViolationException("Cannot create seats for a revoked or canceled flight");
        }

        // Get airplane information
        int col = flight.getAirline().getColFormation();
        int row = flight.getAirline().getRowFormation();
        int counter = 0;

        for (int i = seatCreationRequest.getRowStart(); i <= row; i++) {
            if (counter >= seatCreationRequest.getMaxSeatNumber()) {
                break;
            }
            for (int j = 1; j <= col; j++) {
                if (counter >= seatCreationRequest.getMaxSeatNumber()) {
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
                reservationBySeat(flight, seatCreationRequest.getSeatType());

                // Create and configure the seat
                Seat seat = new Seat();
                seat.setFlight(flight);
                seat.setSeatType(seatCreationRequest.getSeatType());
                seat.setSeatLabel(seatLabel);
                seat.setRow(i);
                seat.setCol(j);
                seat.setPrice(calculatePriceSeat(flight.getPrice(), seatCreationRequest.getSeatType().toSeatTypeName()));
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

    private void reservationBySeat(Flight flight, SeatType seatType) {
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
        reservationBySeat(flight, seat.getSeatType());

        Airplane airplaneResult = airplane.orElseThrow(() -> new DataIntegrityViolationException("Airplane not found for the flight"));

        // Create the seat and set its properties
        Seat seatInst = getCreatedSeatInfo(seat, flight, airplaneResult);


        // Save the seat instance and return
        Seat st = seatsRepo.save(seatInst);
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
    public SeatResponse getSeat(String seatFlag) {
        return seatsRepo.findSeatByFlag(seatFlag).map(SeatMapper::toSeatResponse).orElse(null);
    }

    @Override
    public List<SeatResponse> getAllSeats(String flightNumber) {
        return seatsRepo.findByFlight_FlightNumber(flightNumber).stream().map(SeatMapper::toSeatResponse).collect(Collectors.toList());
    }



}
