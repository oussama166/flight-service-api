package org.jetblue.jetblue.Models.ENUM;

public enum PricingType {
    FIXED,
    DYNAMIC,
    FLEXIBLE,
    DISCOUNTED,
    PREMIUM;

    public static PricingType fromString(String value) {
        for (PricingType type : PricingType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid pricing type: " + value);
    }
}
