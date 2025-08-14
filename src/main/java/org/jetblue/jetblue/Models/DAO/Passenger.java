package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private int age;
    private String passportNumber;
    private LocalDate passportExpiryDate;
    private boolean isUnaccompanied = false;

    // Relation
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "passenger",
            cascade = CascadeType.ALL)
    @Column(name = "documents", nullable = true)
    private Set<Document> documents;
}
