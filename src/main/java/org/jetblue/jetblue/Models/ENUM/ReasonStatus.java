package org.jetblue.jetblue.Models.ENUM;

public enum ReasonStatus {
    FLIGHT_CANCELLED,
    WEATHER_CONDITIONS,
    MAINTENANCE_ISSUES,
    AIR_TRAFFIC_CONTROL,
    CREW_AVAILABILITY,
    SECURITY_CONCERNS,
    DEATH_FAMILY_MEMBER,
    MEDICAL_EMERGENCY,
    OTHER;

    public static String getReason(ReasonStatus status) {
        return switch (status) {
            case FLIGHT_CANCELLED -> "Flight Cancelled";
            case WEATHER_CONDITIONS -> "Weather Conditions";
            case MAINTENANCE_ISSUES -> "Maintenance Issues";
            case AIR_TRAFFIC_CONTROL -> "Air Traffic Control";
            case CREW_AVAILABILITY -> "Crew Availability";
            case SECURITY_CONCERNS -> "Security Concerns";
            case DEATH_FAMILY_MEMBER -> "Death of a Family Member";
            case MEDICAL_EMERGENCY -> "Medical Emergency";
            case OTHER -> "Other";
        };
    }

    public static ReasonStatus fromString(String reason) {
        return switch (reason.toLowerCase()) {
            case "flight cancelled" -> FLIGHT_CANCELLED;
            case "weather conditions" -> WEATHER_CONDITIONS;
            case "maintenance issues" -> MAINTENANCE_ISSUES;
            case "air traffic control" -> AIR_TRAFFIC_CONTROL;
            case "crew availability" -> CREW_AVAILABILITY;
            case "security concerns" -> SECURITY_CONCERNS;
            case "death of a family member" -> DEATH_FAMILY_MEMBER;
            case "medical emergency" -> MEDICAL_EMERGENCY;
            default -> OTHER;
        };
    }
}
