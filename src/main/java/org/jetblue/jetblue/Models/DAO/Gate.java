package org.jetblue.jetblue.Models.DAO;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String gateName;

    @ManyToOne(fetch = LAZY)
    @Column(name = "airport_id", nullable = false)
    private Airport airport;
}
