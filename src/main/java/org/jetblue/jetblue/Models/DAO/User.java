package org.jetblue.jetblue.Models.DAO;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.jetblue.jetblue.Models.ENUM.Gender;
import org.jetblue.jetblue.Models.ENUM.Role;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
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
    @ColumnDefault("false")
    private boolean enabled;
    private String frequentFlyerNumber;
    @Enumerated(EnumType.STRING)
    private Role role = Role.User;

    // Relation
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_preference")
    private UserPreference userPreference;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Booking> bookings;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Passenger> passengers;

    @OneToMany
    @JoinColumn(name= "document_user")
    private List<Document> documents;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CreditCard> creditCards = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken refreshTokens;

}
