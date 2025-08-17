package org.jetblue.jetblue.Service.Implementation;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Mapper.Passenger.PassengerMapper;
import org.jetblue.jetblue.Mapper.Passenger.PassengerRequest;
import org.jetblue.jetblue.Mapper.Passenger.PassengerResponse;
import org.jetblue.jetblue.Models.DAO.Passenger;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Repositories.PassengerRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.PassengerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PassengerImpl implements PassengerService {
    PassengerRepo passengerRepo;
    UserRepo userRepo;

    @Override
    public PassengerResponse createPassenger(PassengerRequest passenger) {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger cannot be null");
        }
        Optional<User> userInfo = userRepo.findByUsername(passenger.userName());
        if (userInfo.isEmpty()) {
            throw new IllegalArgumentException("User not found with username: " + passenger.userName());
        }
        Passenger newPassenger = Passenger.builder()
                .firstName(passenger.firstName())
                .middleName(passenger.middleName())
                .lastName(passenger.lastName())
                .birthDate(passenger.birthDate())
                .age(Period.between(userInfo.get().getBirthday(), LocalDate.now()).getYears())
                .passportNumber(passenger.passportNumber())
                .passportExpiryDate(passenger.passportExpiryDate())
                .user(userInfo.get())
                .hasAccount(passenger.isUser())
                .build();
        try {
            Passenger savedPassenger = passengerRepo.save(newPassenger);
        } catch (Exception e) {
            throw new RuntimeException("Error saving passenger: " + e.getMessage());
        }
        return PassengerMapper.toPassengerResponse(newPassenger);

    }

    @Override
    public Passenger getPassengerById(Long id) {
        return null;
    }
}
