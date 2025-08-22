package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;
import org.jetblue.jetblue.Models.ENUM.Notification;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatPreference;
    private String mealPreference;
    @Enumerated(EnumType.STRING)
    private Notification notificationPreference = Notification.PUSH;


    // Relation
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
