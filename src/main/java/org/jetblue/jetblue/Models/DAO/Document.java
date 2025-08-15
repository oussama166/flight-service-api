package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;
import org.jetblue.jetblue.Models.ENUM.DocumentType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private DocumentType type;
    @Column(
            nullable = false,
            unique = true
    )
    private String path;
    @Column(
            updatable = true
    )
    private Timestamp created;

    // Relation
    @ManyToOne(fetch = FetchType.EAGER)
    private Passenger passenger;


    @PrePersist
    protected void onCreate() {
        created = Timestamp.valueOf(LocalDateTime.now());
    }
}
