package org.jetblue.jetblue.Mapper.Passenger;

import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Models.DAO.Passenger;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PassengerMapper {
    public static UserRepo userRepo;

    public static PassengerResponse toPassengerResponse(Passenger passenger) {
        if (passenger == null) {
            return null;
        }

        return PassengerResponse.builder()
                .firstName(passenger.getFirstName())
                .lastName(passenger.getLastName())
                .email(passenger.getUser().getEmail())
                .phoneNumber(passenger.getUser().getPhone())
                .build();
    }

    public static Passenger toPassenger(PassengerRequest passengerRequest) {
        if (passengerRequest == null) {
            return null;
        }

        return Passenger.builder()
                .firstName(passengerRequest.firstName())
                .middleName(passengerRequest.middleName())
                .lastName(passengerRequest.lastName())
                .passportExpiryDate(passengerRequest.passportExpiryDate())
                .user(
                        userRepo.findByUsername(passengerRequest.userName()).orElseThrow(() -> new RuntimeException("User not found"))
                )
                .passportNumber(passengerRequest.passportNumber())
                .build();
    }
}
