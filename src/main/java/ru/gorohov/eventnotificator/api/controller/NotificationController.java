package ru.gorohov.eventnotificator.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gorohov.eventnotificator.api.dto.ListOfIdsToReadDto;
import ru.gorohov.eventnotificator.api.dto.NotificationDto;
import ru.gorohov.eventnotificator.domain.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications() {
        log.info("Get mapping of unread notifications");
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForCurrentUser());
    }

    @PostMapping
    public ResponseEntity<Void> readNotifications(@RequestBody ListOfIdsToReadDto notificationIdsDto) {
        log.info("Post mapping for read notifications");
        notificationService.readNotificationsForCurrentUser(notificationIdsDto.getNotificationIds());
        return ResponseEntity.status(204).build();
    }

}
