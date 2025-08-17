package org.jetblue.jetblue.Models.ENUM;

public enum SeatType {
    FIRST_CLASS,SECOND_CLASS,ECONOMY_CLASS;

    public static SeatType fromString(String seatType) {
        return switch (seatType.toUpperCase()) {
            case "FIRST_CLASS" -> FIRST_CLASS;
            case "SECOND_CLASS" -> SECOND_CLASS;
            case "ECONOMY_CLASS" -> ECONOMY_CLASS;
            default -> throw new IllegalArgumentException("Unknown seat type: " + seatType);
        };
    }
    public String toSeatTypeName() {
        return switch (this) {
            case FIRST_CLASS -> "FIRST";
            case SECOND_CLASS -> "BUSINESS";
            case ECONOMY_CLASS -> "ECONOMY";
        };
    }
}
