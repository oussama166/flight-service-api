package org.jetblue.jetblue.Models.DTO;


import org.jetblue.jetblue.Models.ENUM.Gender;

import java.time.LocalDate;
import java.util.Date;

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

    public UserBasicDTO() {}
    public UserBasicDTO(String username, String name, String lastName, String middleName, String email, String phone, String address, String origin, LocalDate birthday, Gender gender, boolean verified) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.origin = origin;
        Birthday = birthday;
        this.gender = gender;
        this.verified = verified;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getOrigin() {
        return origin;
    }

    public LocalDate getBirthday() {
        return Birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isVerified() {
        return verified;
    }
}
