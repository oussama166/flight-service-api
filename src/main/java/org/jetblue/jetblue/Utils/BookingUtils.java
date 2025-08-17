package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Passenger;
import org.jetblue.jetblue.Models.DAO.Seat;

import java.util.ArrayList;
import java.util.List;

public class BookingUtils {


    public static String validatePassenger(Passenger passenger, Flight flightDetails) {
        List<String> errors = new ArrayList<>();

        // Validate age rules
        if (passenger.getAge() < 0) {
            errors.add("Age cannot be negative.");
        } else if (passenger.getAge() < 2) {
            if (!flightDetails.isAllowsBassinet()) {
                errors.add("This airline does not allow bassinets for infants.");
            }
        } else if (passenger.getAge() < 12 && passenger.isUnaccompanied() && !flightDetails.isAllowsUnaccompaniedMinors()) {
            errors.add("Unaccompanied minors are not allowed on this flight.");
        }

        // Validate documents
        if (passenger.getDocuments() == null || passenger.getDocuments().isEmpty()) {
            errors.add("Required documents are missing.");
        } else if (passenger.isUnaccompanied() && !passenger.getDocuments().contains("Notarized Consent")) {
            errors.add("Notarized consent is required for unaccompanied minors.");
        }


        return (errors.isEmpty()) ? null : String.join(", ", errors);

    }

    public static double calculateTicketPrice(Passenger passenger, Seat SeatDetails) {
        if (passenger.getAge() < 2) {
            return SeatDetails.getPrice() * 0.1; // 90% discount for infants
        } else if (passenger.getAge() < 12) {
            return SeatDetails.getPrice() * 0.75; // 25% discount for children
        } else {
            return SeatDetails.getPrice(); // Full fare for others
        }
    }

    public static List<String> getAvailableServices(Passenger passenger, Flight flightDetails) {
        List<String> services = new ArrayList<>();

        if (passenger.getAge() < 2 && flightDetails.isAllowsBassinet()) {
            services.add("Bassinet");
        }
        if (passenger.getAge() < 12 && flightDetails.isProvidesChildMeals()) {
            services.add("Child-friendly meals");
        }
        if (passenger.getAge() < 12) {
            services.add("Extra baggage allowance");
        }

        return services;
    }
}
