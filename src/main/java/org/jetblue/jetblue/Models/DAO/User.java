package org.jetblue.jetblue.Models.DAO;


import org.jetblue.jetblue.Models.ENUM.Gender;
import org.jetblue.jetblue.Models.ENUM.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false,unique = true)
    private String username;
    private String name;
    private String lastName;
    @Column(nullable = true)
    private String middleName;
    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    private String address;
    private String origin;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean verified;
    @Column(nullable = true)
    private String frequentFlyerNumber;
    @Enumerated(EnumType.STRING)
    private Role role = Role.User;

    // Relation
    @OneToOne
    @JoinColumn(name = "user-preference")
    private UserPreference userPreference;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Booking> bookings;

}
