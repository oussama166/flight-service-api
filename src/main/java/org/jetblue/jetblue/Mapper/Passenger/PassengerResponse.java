package org.jetblue.jetblue.Mapper.Passenger;

import lombok.Builder;

@Builder
public record PassengerResponse(
        String firstName,
        String lastName,
        String middleName,
        int age,
        boolean isUnaccompanied,
        String passportNumber,
        boolean isUser,
        String email,
        String phoneNumber
) {
}
