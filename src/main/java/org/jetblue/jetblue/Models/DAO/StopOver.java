package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "stop_over",
        uniqueConstraints = @UniqueConstraint(columnNames = {"flight_id", "stop_order"})
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StopOver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int stopOrder;
    private LocalDateTime arrivalTime;
    private LocalDateTime departureTime;
    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "airport_id", nullable = false)
    private Airport airport;

    public Duration getStopDuration() {
        if (arrivalTime != null && departureTime != null) {
            return Duration.between(arrivalTime, departureTime);
        }
        return Duration.ZERO;
    }

    public String getFormattedStopDuration() {
        Duration duration = getStopDuration();
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return hours + "h " + minutes + "m";
    }


}

