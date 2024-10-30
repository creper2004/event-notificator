package ru.gorohov.eventnotificator.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gorohov.eventnotificator.db.entity.EventChangeEntity;

public interface EventChangesRepository extends JpaRepository<EventChangeEntity, Long> {


}
