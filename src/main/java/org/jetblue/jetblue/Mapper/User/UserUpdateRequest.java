package org.jetblue.jetblue.Mapper.User;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import org.jetblue.jetblue.Models.ENUM.Gender;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record UserUpdateRequest  (
        String name,
        String lastName,
        String middleName,
        Gender gender,
        String username,
        String address,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthday,
        String origin,
        String phone,
        @Email(message = "Email format invalid")
        String email,
        String frequentFlyerNumber
) {
}
