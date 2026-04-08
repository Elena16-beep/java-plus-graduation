package ru.practicum.ewm.stats.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interactions")
public class UserInteraction {

    @EmbeddedId
    private UserEventId id;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
