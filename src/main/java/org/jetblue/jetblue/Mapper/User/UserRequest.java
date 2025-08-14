package org.jetblue.jetblue.Mapper.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.jetblue.jetblue.Models.ENUM.Gender;
import org.jetblue.jetblue.Models.ENUM.Role;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UserRequest(
        @NotNull(message = "first name must be fill")
        String name,
        @NotNull(message = "last name must be fill")
        String lastName,
        String middleName,
        @NotNull(message = "Gender must be inserted")
        Gender gender,
        @NotNull(message = "Username must be insrted")
        String username,
        @NotNull(message = "Address not null filed")
        String address,
        @NotNull(message = "Birthday not null filed")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday,
        @NotNull(message = "Origin not null filed")
        String origin,
        @NotNull(message = "Phone number is not null filed")
        String phone,
        @NotNull(message = "Password must be inserted")
        String password,
        @NotNull(message = "Email must be inserted")
        @Email(message = "Email format invalid")
        String email,
        Role role,
        @DefaultValue(value = "false")
        boolean verified,
        String passportNumber,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate passportExpirationDate

        ) {
    public UserRequest {
        if (role == null) {
            role = Role.User;
        }

    }
}
