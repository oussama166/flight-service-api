package org.jetblue.jetblue.Utils;

import org.jetblue.jetblue.Models.DAO.Flight;

import java.math.BigDecimal;

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
}
