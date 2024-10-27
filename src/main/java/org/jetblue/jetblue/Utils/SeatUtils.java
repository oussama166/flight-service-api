package org.jetblue.jetblue.Utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
                seats.add(label + String.valueOf(row)); // e.g., A1, B1, ..., F50
            }
        }

        return seats;
    }
    public static String generateSingleSeat(int row, int seatPosition) {
        char seatLabel = (char) ('A' + seatPosition - 1); // Convert seat position to corresponding letter
        return seatLabel + String.valueOf(row); // e.g., A1, B1, ..., F1
    }
}
