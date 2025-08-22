package org.jetblue.jetblue.Mapper.User;

import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import org.jetblue.jetblue.Models.ENUM.Gender;

import java.time.LocalDate;

@Builder
public record UserResponseBasic(
        String username,
        String name,
        String lastName,
        String middleName,
        String email,
        String phone,
        String address,
        String origin,
        LocalDate Birthday,
        Gender gender,
        @ColumnDefault(value = "true")
        boolean verified
) {
}
