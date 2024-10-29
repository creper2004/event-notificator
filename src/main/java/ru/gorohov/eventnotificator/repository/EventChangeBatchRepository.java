package ru.gorohov.eventnotificator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EventChangeBatchRepository extends JpaRepository<EventChangesBatchEntity, Long> {

    @Modifying
    @Query(value = """
DELETE FROM event_notification
WHERE event_notification.change_batch_id IN (
    SELECT id FROM event_changes_batch
    WHERE CURRENT_TIMESTAMP - timestamp > make_interval(days := 7)
)
""", nativeQuery = true)
    void deleteOldEventNotification();

    @Modifying
    @Query(value = """
DELETE FROM event_change 
WHERE event_change.change_batch_id IN (
    SELECT id FROM event_changes_batch
    WHERE CURRENT_TIMESTAMP - timestamp > make_interval(days := 7)
)
""", nativeQuery = true)
    void deleteOldEventChanges();

    @Modifying
    @Query(value = """
    delete from event_changes_batch AS e
    WHERE CURRENT_TIMESTAMP - e.timestamp > make_interval(days := 7)
""", nativeQuery = true)
    void deleteOldChangesBatch();
}