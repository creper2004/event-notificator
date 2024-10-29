package ru.gorohov.eventnotificator.api;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.gorohov.eventnotificator.repository.EventChangeEntity;
import ru.gorohov.eventnotificator.repository.EventNotificationEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class NotificationDto {

    private Long notificationId;

    private Long eventId;

    private Long ownerId;

    private LocalDateTime timestamp;

    private List<ChangesDto> changes;


}
