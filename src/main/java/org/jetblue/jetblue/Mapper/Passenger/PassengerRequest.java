package org.jetblue.jetblue.Mapper.Passenger;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PassengerRequest(
        String firstName,
        String middleName,
        String lastName,
        LocalDate birthDate,
        String email,
        String phoneNumber,
        String passportNumber,
        LocalDate passportExpiryDate,
        String userName,
        boolean isUser
) {
}
