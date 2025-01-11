package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;

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
    private boolean isUnaccompanied;

    // Relation
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "passenger")
    private Set<Document> documents;
}
