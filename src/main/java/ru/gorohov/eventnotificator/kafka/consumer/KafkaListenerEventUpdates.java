package ru.gorohov.eventnotificator.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.gorohov.eventnotificator.kafka.message.InfoOfEditedEvent;
import ru.gorohov.eventnotificator.domain.NotificationService;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListenerEventUpdates {

    private final NotificationService notificationService;


    @KafkaListener(topics = "changed-event-topic",
            containerFactory = "kafkaListenerContainerFactory", groupId = "getUpdatesOfEvents")
    void listenerUpdatedEvent(InfoOfEditedEvent message) {
        notificationService.putNewNotification(message);
        log.info("Received message through MessageConverterUserListener [{}]", message);
    }


}
