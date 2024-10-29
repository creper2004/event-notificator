package ru.gorohov.eventnotificator.scheduling;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import ru.gorohov.eventnotificator.repository.EventChangeBatchRepository;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class DeleterOldNotifications {

    private final Logger log = LoggerFactory.getLogger(DeleterOldNotifications.class);
    private final EventChangeBatchRepository eventChangeBatchRepository;

    @Scheduled(cron = "${delete.notifications.cron}")
    @Transactional
    public void deleteOldNotifications() {
        log.info("Deleting old notifications");
        eventChangeBatchRepository.deleteOldEventNotification();
        eventChangeBatchRepository.deleteOldEventChanges();
        eventChangeBatchRepository.deleteOldChangesBatch();
    }

}