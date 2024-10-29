package ru.gorohov.eventnotificator.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gorohov.InfoOfEditedEvent;
import ru.gorohov.eventnotificator.domain.NotificationService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications() {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForCurrentUser());
    }

    @PostMapping
    public ResponseEntity<Void> readNotifications(@RequestBody ListOfIdsToReadDto notificationIdsDto) {
        notificationService.readNotificationsForCurrentUser(notificationIdsDto.getNotificationIds());
        return ResponseEntity.status(204).build();
    }

//    @GetMapping("/q")
//    public ResponseEntity<?> q() {
//        return ResponseEntity.ok("qwerty");
//    }

}
