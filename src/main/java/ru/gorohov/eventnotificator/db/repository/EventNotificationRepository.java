package ru.gorohov.eventnotificator.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gorohov.eventnotificator.db.entity.EventNotificationEntity;

import java.util.List;

public interface EventNotificationRepository extends JpaRepository<EventNotificationEntity, Long> {

    List<EventNotificationEntity> findByUserIdAndIsReadFalse(Long userId);


    @Modifying
    @Query("UPDATE EventNotificationEntity e SET e.isRead = :isRead WHERE e.id IN :ids")
    void updateIsReadByIds(@Param("ids") List<Long> ids, @Param("isRead") boolean isRead);

}