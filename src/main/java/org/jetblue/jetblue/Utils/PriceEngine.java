package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Flight;
import org.jetblue.jetblue.Models.DAO.Seat;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Models.ENUM.SeatType;

import java.time.Duration;
import java.time.LocalDateTime;

public class PriceEngine {
    private static final double BASE_RATE_PER_KM = 0.15;   // USD per km
    private static final double FUEL_SURCHARGE_DEFAULT = 50.0;   // USD
    private static final double AIRPORT_FEES_DEFAULT = 60.0;     // USD

    static double baseRatePerKm(double km) {
        if (km < 1500) return 0.16;
        if (km < 3000) return 0.135; // 0.14 si tu veux ~350$, 0.135 pour viser ~330$
        if (km < 6000) return 0.09;
        return 0.05;
    }

    static double fuelByDistance(double km) {
        if (km < 1500) return 25;
        if (km < 3000) return 35;
        if (km < 6000) return 55;
        return 75;
    }

    static double feesByDistance(double km) {
        if (km < 1500) return 40;
        if (km < 3000) return 50;
        if (km < 6000) return 60;
        return 75;
    }

    // These methods implement a simple dynamic pricing model based on flight occupancy rates
    public static double dynamicPricing(Flight flight) {
        double basePrice = flight.getPrice();


        double economyOccupancy = flight.getMaxThirdClass() > 0
                ? (double) flight.getThirdClassReserved() / flight.getMaxThirdClass()
                : 0.0;

        double businessOccupancy = flight.getMaxSecondClass() > 0
                ? (double) flight.getSecondClassReserved() / flight.getMaxSecondClass()
                : 0.0;

        double firstOccupancy = flight.getMaxFirstClass() > 0
                ? (double) flight.getFirstClassReserved() / flight.getMaxFirstClass()
                : 0.0;

        // Pick the HIGHEST occupancy rate as main driver of demand
        double overallOccupancy = Math.max(economyOccupancy,
                Math.max(businessOccupancy, firstOccupancy));

        double demandFactor;
        if (overallOccupancy < 0.3) {
            demandFactor = 0.0;
        } else if (overallOccupancy < 0.7) {
            demandFactor = 0.1;
        } else if (overallOccupancy < 0.9) {
            demandFactor = 0.2;
        } else {
            demandFactor = 0.3; // almost full
        }

        // Final price
        return basePrice + (basePrice * demandFactor);
    }
    public static double dynamicPricingV1(Flight flight) {
        double basePrice = flight.getPrice();

        // Total seats and reserved seats
        int totalSeats = flight.getMaxFirstClass() + flight.getMaxSecondClass() + flight.getMaxThirdClass();
        int totalReserved = flight.getFirstClassReserved() + flight.getSecondClassReserved() + flight.getThirdClassReserved();

        // Weighted occupancy (capped at 1.0)
        double weightedOccupancy = totalSeats > 0 ? (double) totalReserved / totalSeats : 0.0;
        weightedOccupancy = Math.min(weightedOccupancy, 1.0);

        // Continuous demand factor (0.0 to 0.3)
        double maxDemandFactor = 0.3;
        double demandFactor = weightedOccupancy * maxDemandFactor;

        // Final price
        return basePrice + (basePrice * demandFactor);
    }

    public static double flexiblePricing(Seat seat) {
        double basePrice = seat.getPrice();
        String seatClass = seat.getSeatType().toSeatTypeName();

        LocalDateTime departureDate = seat.getFlight().getDepartureTime();
        LocalDateTime now = LocalDateTime.now();
        long daysUntilDeparture = Duration.between(now, departureDate).toDays();

        double timeFactor = getTimeFactor(daysUntilDeparture);
        double classFactor = classFactorDiscount(seatClass);

        // Final price
        return basePrice + (basePrice * timeFactor) + (basePrice * classFactor);

    }


    public static double discountedPricing(SeatType seatType, double basePrice, int quantity) {


        String seatClass = seatType.toSeatTypeName();

        double discount;
        if (quantity >= 5) {
            discount = 0.15;
        } else if (quantity == 4) {
            discount = 0.10;
        } else if (quantity == 3) {
            discount = 0.05;
        } else {
            discount = 0.0;
        }

        double classFactor = classFactorDiscount(seatClass);
        // Final price
        return (basePrice + (basePrice * classFactor)) * (1 - discount);
    }


    // Pricing based on seat class automatically adjusts the price based on the seat class
    public static double calculatePriceSeat(double basePrice, String seatClass) {
        return switch (seatClass.toUpperCase()) {
            case "ECONOMY" -> basePrice; // Economy class price
            case "BUSINESS" -> basePrice * 1.5; // Business class price
            case "FIRST" -> basePrice * 2.0; // First class price
            default -> throw new IllegalArgumentException("Invalid seat class: " + seatClass);
        };
    }

    public static double calculatePriceFlight(Flight flight) {
        double fuelSurcharge = fuelByDistance(flight.getDistance());
        double airportFees = feesByDistance(flight.getDistance());

        return fuelSurcharge + airportFees + (0.15 + 1 * flight.getDistance());
    }

    public static double calculatePriceFlight(double distanceKm) {
        double rate = baseRatePerKm(distanceKm);
        double fuel = fuelByDistance(distanceKm);
        double fees = feesByDistance(distanceKm);
        double base = distanceKm * rate;

        double raw = base + fuel + fees;

        double seasonMult = 0.95;     // -5% hors saison
        double advanceMult = 0.98;    // -2% si J-30+
        double loadFactorMult = 1.00; // 1.00 par défaut


        return Math.round(raw * seasonMult * advanceMult * loadFactorMult);
    }


    // Helper method to determine class factor discount
    private static double classFactorDiscount(String seatClass) {
        double classFactor;
        switch (seatClass.toUpperCase()) {
            case "ECONOMY" -> classFactor = 0.0; // no change for economy
            case "BUSINESS" -> classFactor = 0.20; // 20% increase for business
            case "FIRST" -> classFactor = 0.30; // 30% increase for first class
            default -> throw new IllegalArgumentException("Invalid seat class: " + seatClass);
        }
        return classFactor;
    }

    private static double getTimeFactor(long daysUntilDeparture) {
        double timeFactor;
        if (daysUntilDeparture > 30) {
            timeFactor = -0.20; // 10% discount for booking more than 30 days in advance
        } else if (daysUntilDeparture > 14) {
            timeFactor = -0.10; // 5% discount for booking between 15 and 30 days in advance
        } else if (daysUntilDeparture > 7) {
            timeFactor = 0.0; // no change for booking between 8 and 14 days in advance
        } else {
            timeFactor = 0.10; // 10% increase for booking within the last week
        }
        return timeFactor;
    }

    public static double refundPenalty(long daysUntilDeparture) {
        if (daysUntilDeparture > 30) {
            return 0.10; // 10% penalty for refunding more than 30 days in advance
        } else if (daysUntilDeparture > 14) {
            return 0.20; // 20% penalty for refunding between 15 and 30 days in advance
        } else if (daysUntilDeparture > 7) {
            return 0.30; // 30% penalty for refunding between 8 and 14 days in advance
        } else {
            return 0.50; // 50% penalty for refunding within the last week
        }
    }
}
