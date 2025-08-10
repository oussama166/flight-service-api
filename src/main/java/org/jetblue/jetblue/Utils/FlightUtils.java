package org.jetblue.jetblue.Utils;

import java.time.LocalDateTime;
import java.time.Duration;

public class FlightUtils {
    // Earth radius in kilometers
    private static final int EARTH_RADIUS = 6371;

    // Airplane speed in km/h
    private static final int SPEED_AIRPLANE = 900;

    // Calculate distance between two lat/lon points in kilometers
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    // Estimate time to reach destination in hours based on distance and speed
    public static double estimateTimeToReachDes(double dist) {
        return dist / SPEED_AIRPLANE;
    }

    // Calculate arrival LocalDateTime given departure time and lat/lon of origin & destination
    public static LocalDateTime calculateArrivalTime(LocalDateTime departureTime,
                                                     double lat1, double lon1,
                                                     double lat2, double lon2) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        double flightDurationHours = estimateTimeToReachDes(distance);

        // Convert flight duration to minutes for adding to departureTime
        long minutesToAdd = (long)(flightDurationHours * 60);

        // Calculate and return arrival time
        return departureTime.plusMinutes(minutesToAdd);
    }
}
