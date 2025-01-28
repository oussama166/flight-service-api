package org.jetblue.jetblue.Utils;

import java.util.ArrayList;
import java.util.List;


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
}
