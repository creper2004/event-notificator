package ru.gorohov.eventnotificator;

import org.springframework.stereotype.Component;
import ru.gorohov.HistoryOfFields;
import ru.gorohov.InfoOfEditedEvent;
import ru.gorohov.eventnotificator.api.ChangesDto;
import ru.gorohov.eventnotificator.api.NotificationDto;
import ru.gorohov.eventnotificator.repository.EventChangeEntity;
import ru.gorohov.eventnotificator.repository.EventChangesBatchEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class EventNotificationConvector {

    public List<EventChangeEntity> getEventChangeEntities(InfoOfEditedEvent infoOfEditedEvent) {
        List<EventChangeEntity> eventChangeEntities = new ArrayList<>();
        Class<?> objClass = infoOfEditedEvent.getClass();

        try {
            for (Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (HistoryOfFields.class.isAssignableFrom(field.getType())) {
                    HistoryOfFields<?> historyOfFields = (HistoryOfFields<?>) field.get(infoOfEditedEvent);

                    if (historyOfFields != null) {
                        String fieldName = field.getName();
                        Object oldFieldValue = historyOfFields.getOldField();
                        Object newFieldValue = historyOfFields.getNewField();

                        if (!Objects.equals(oldFieldValue, newFieldValue)) {
                            String oldFieldValueStr = oldFieldValue != null ? oldFieldValue.toString() : null;
                            String newFieldValueStr = newFieldValue != null ? newFieldValue.toString() : null;

                            var curChange = EventChangeEntity.builder()
                                    .id(null)
                                    .fieldName(fieldName)
                                    .oldValue(oldFieldValueStr)
                                    .newValue(newFieldValueStr)
                                    .changesBatch(null)
                                    .build();
                            eventChangeEntities.add(curChange);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields", e);
        }
        return eventChangeEntities;
    }

    public NotificationDto fromEntityBatchToDto(EventChangesBatchEntity eventChangesBatchEntity) {
        return NotificationDto.builder()
                .notificationId(eventChangesBatchEntity.getId()) //todo
                .eventId(eventChangesBatchEntity.getEventId())
                .ownerId(eventChangesBatchEntity.getOwnerId())
                .timestamp(eventChangesBatchEntity.getTimestamp())
                .changes(eventChangesBatchEntity.getChanges().stream().map(this::fromEntityChangeToDto).toList())
                .build();
    }

    public ChangesDto fromEntityChangeToDto(EventChangeEntity eventChangeEntity) {
        return ChangesDto.builder()
                .fieldName(eventChangeEntity.getFieldName())
                .oldValue(eventChangeEntity.getOldValue())
                .newValue(eventChangeEntity.getNewValue())
                .build();
    }

}