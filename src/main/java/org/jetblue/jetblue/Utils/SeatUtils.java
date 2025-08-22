package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Airplane;
import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DTO.SeatCreate;

import java.util.ArrayList;
import java.util.List;

import static org.jetblue.jetblue.Utils.PriceEngine.calculatePriceSeat;


public class SeatUtils {
    public static List<String> generateSeats(int numRows, int seatsPerRow) {
        List<String> seats = new ArrayList<>();
        char[] rowLabels = new char[seatsPerRow];

        // Generate row labels (A, B, C, ... based on seatsPerRow)
        for (int i = 0; i < seatsPerRow; i++) {
            rowLabels[i] = (char) ('A' + i);
        }

        // Generate seat labels
        for (int row = 1; row <= numRows; row++) {
            for (char label : rowLabels) {
                seats.add(label + String.valueOf(row));
            }
        }

        return seats;
    }
    public static String generateSingleSeat(int row, int seatPosition) {
        char seatLabel = (char) ('A' + seatPosition);
        return seatLabel + String.valueOf(row);
    }

    public static Seat getCreatedSeatInfo(SeatCreate seat, Flight flight, Airplane airplane) {

        double seatPrice = calculatePriceSeat(flight.getPrice(),seat.getSeatType().name());


        Seat seatInst = new Seat();
        seatInst.setPrice(seatPrice);
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
