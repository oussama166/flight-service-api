package org.jetblue.jetblue.Utils;

public class FlightUtils {
    private static final int EARTH_RADIUS = 6371; // Earth radius in kilometers
    private static final int SPEED_AIRPLANE = 900; // Airplane speed

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        return EARTH_RADIUS * c;
    }
    public static double estimateTimeToReachDes(double dist) {
        return dist / SPEED_AIRPLANE;
    }
}
