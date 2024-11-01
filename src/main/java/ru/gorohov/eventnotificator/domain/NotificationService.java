package ru.gorohov.eventnotificator.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gorohov.eventnotificator.kafka.message.InfoOfEditedEvent;
import ru.gorohov.eventnotificator.utils.EventNotificationConvector;
import ru.gorohov.eventnotificator.api.dto.NotificationDto;
import ru.gorohov.eventnotificator.db.entity.EventChangeEntity;
import ru.gorohov.eventnotificator.db.entity.EventChangesBatchEntity;
import ru.gorohov.eventnotificator.db.entity.EventNotificationEntity;
import ru.gorohov.eventnotificator.db.repository.EventChangeBatchRepository;
import ru.gorohov.eventnotificator.db.repository.EventChangesRepository;
import ru.gorohov.eventnotificator.db.repository.EventNotificationRepository;
import ru.gorohov.eventnotificator.secured.GetCurrentUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final EventChangeBatchRepository eventChangesBatchRepository;
    private final EventNotificationRepository eventNotificationRepository;
    private final EventChangesRepository eventChangesRepository;

    private final EventNotificationConvector eventNotificationConvector;
    private final GetCurrentUserService getCurrentUserService;


    @Transactional
    public void putNewNotification(InfoOfEditedEvent infoOfEditedEvent) {

        List<EventChangeEntity> changes = eventNotificationConvector.getEventChangeEntities(infoOfEditedEvent);

        var eventChangesBatch =  EventChangesBatchEntity.builder()
                .eventId(infoOfEditedEvent.getEventId())
                .timestamp(LocalDateTime.now())
                .ownerId(infoOfEditedEvent.getOwnerId())
                .id(null)
                .changes(changes)
                .build();
        changes.forEach(c  -> c.setChangesBatch(eventChangesBatch));
        eventChangesBatchRepository.save(eventChangesBatch);

        List<EventNotificationEntity> eventNotificationEntities = new ArrayList<>();
        infoOfEditedEvent.getRegisteredUsersId()
                .forEach(id -> {
                    eventNotificationEntities.add(
                            EventNotificationEntity.builder()
                                    .userId(id)
                                    .isRead(false)
                                    .changesBatch(eventChangesBatch)
                                    .id(null)
                                    .build()
                    );
                });
        eventChangesBatch.setNotifications(eventNotificationEntities);
        log.info("Added new notification from kafka");
        eventChangesRepository.saveAll(changes);
        eventNotificationRepository.saveAll(eventNotificationEntities);

    }


    public List<NotificationDto> getUnreadNotificationsForCurrentUser() {

        var foundedUnreadNotify = getUnreadNotificationsEntitiesForCurrentUser();

        List<NotificationDto> unreadNotifications = foundedUnreadNotify.stream()
                .map(record -> NotificationDto.builder()
                        .notificationId(record.getId())
                        .eventId(record.getChangesBatch().getEventId())
                        .ownerId(record.getChangesBatch().getOwnerId())
                        .timestamp(record.getChangesBatch().getTimestamp())
                        .changes(record.getChangesBatch().getChanges().stream()
                                .map(eventNotificationConvector::fromEntityChangeToDto).toList())
                        .build())
                .toList();
        log.info("Getting unread notifications for current user: {}", foundedUnreadNotify);
        return unreadNotifications;
    }

    @Transactional
    public void readNotificationsForCurrentUser(List<Long> notificationIdsToMark) {

        var foundedUnreadNotifyIds = getUnreadNotificationsEntitiesForCurrentUser()
                .stream()
                .map(EventNotificationEntity::getId)
                .toList();

        List<Long> idsToUpdate = notificationIdsToMark.stream()
                .filter(foundedUnreadNotifyIds::contains)
                .collect(Collectors.toList());
        log.info("Trying marking unread notifications for current user: {}", idsToUpdate);
        if (!idsToUpdate.isEmpty()) {
            eventNotificationRepository.updateIsReadByIds(idsToUpdate, true);
        }

    }

    private List<EventNotificationEntity> getUnreadNotificationsEntitiesForCurrentUser() {
        var currentUser = getCurrentUserService.getCurrentUser();
        var foundedUnreadNotify = eventNotificationRepository.findByUserIdAndIsReadFalse(currentUser.getId());
        log.info("Is got unread notifies: {}", foundedUnreadNotify);
        return foundedUnreadNotify;
    }


}