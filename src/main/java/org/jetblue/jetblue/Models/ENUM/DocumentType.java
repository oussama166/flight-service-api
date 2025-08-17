package org.jetblue.jetblue.Models.ENUM;

import lombok.Getter;

@Getter
public enum DocumentType {
    // --- Identity and Citizenship Documents ---
    PASSPORT("Passport"),
    NATIONAL_ID_CARD("National ID Card"),
    DRIVER_LICENSE("Driver's License"),
    BIRTH_CERTIFICATE("Birth Certificate"),
    SOCIAL_SECURITY_CARD("Social Security Card"),
    MARRIAGE_CERTIFICATE("Marriage Certificate"),

    // --- Travel and Immigration Documents ---
    VISA("Visa"),
    ELECTRONIC_VISA("Electronic Visa (e-Visa)"),
    IMMIGRATION_CARD("Immigration Card"),
    RESIDENCE_PERMIT("Residence Permit"),
    WORK_PERMIT("Work Permit"),
    TRAVEL_AUTHORIZATION("Travel Authorization (e.g., ESTA, ETA)"),
    SPOUSE_VISA("Spouse Visa"),
    STUDENT_VISA("Student Visa"),
    TRANSIT_VISA("Transit Visa"),

    // --- Aviation-Specific Documents ---
    BOARDING_PASS("Boarding Pass"),
    AIRLINE_TICKET("Airline Ticket"),
    TSA_PRECHECK_CARD("TSA PreCheck Card"),
    GLOBAL_ENTRY_CARD("Global Entry Card"),
    NEXUS_CARD("NEXUS Card"),

    // --- Medical and Health Documents ---
    HEALTH_CERTIFICATE("Health Certificate"),
    VACCINE_CARD("Vaccine Card (e.g., COVID-19)"),
    MEDICAL_CLEARANCE_FORM("Medical Clearance Form"),

    // --- Special Purpose Documents ---
    MILITARY_ID("Military ID"),
    DS3053_AFFIDAVIT("DS-3053 Affidavit"),
    GOVERNMENT_ISSUED_CERTIFICATE("Government-Issued Certificate"),
    AUTHORIZATION_LETTER("Authorization Letter");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }

}