package org.jetblue.jetblue.Models.DTO;


import lombok.*;
import org.jetblue.jetblue.Models.ENUM.Gender;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicDTO {
    private String username;
    private String name;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private String address;
    private String origin;
    private LocalDate Birthday;
    private Gender gender;
    private boolean verified;


}
