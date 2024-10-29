package ru.gorohov.eventnotificator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventChangesRepository extends JpaRepository<EventChangeEntity, Long> {


}
