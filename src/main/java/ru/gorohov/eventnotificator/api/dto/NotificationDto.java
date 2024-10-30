package ru.gorohov.eventnotificator.api.dto;

import lombok.Builder;
import lombok.Data;

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
