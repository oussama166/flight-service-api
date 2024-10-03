package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatPreference;
    private String mealPreference;
    private String notificationPreference;

    // Relation
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user-id")
    private User user;
}
