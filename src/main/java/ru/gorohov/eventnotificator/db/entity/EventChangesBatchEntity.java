package ru.gorohov.eventnotificator.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event_changes_batch")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventChangesBatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "changesBatch", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EventChangeEntity> changes;

    @OneToMany(mappedBy = "changesBatch", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EventNotificationEntity> notifications;

}