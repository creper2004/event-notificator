package ru.gorohov.eventnotificator.kafka.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data

public class InfoOfEditedEvent {

    private Long eventId;
    private Long ownerId;
    private HistoryOfFields<String> name;
    private HistoryOfFields<Integer> maxPlaces;
    private HistoryOfFields<LocalDateTime> date;
    private HistoryOfFields<Integer> cost;
    private HistoryOfFields<Integer> duration;
    private HistoryOfFields<Long> locationId;
    private HistoryOfFields<String> status;
    private List<Long> registeredUsersId;

    @JsonCreator
    public InfoOfEditedEvent(
            @JsonProperty("eventId") Long eventId,
            @JsonProperty("ownerId") Long ownerId,
            @JsonProperty("name") HistoryOfFields<String> name,
            @JsonProperty("maxPlaces") HistoryOfFields<Integer> maxPlaces,
            @JsonProperty("date") HistoryOfFields<LocalDateTime> date,
            @JsonProperty("cost") HistoryOfFields<Integer> cost,
            @JsonProperty("duration") HistoryOfFields<Integer> duration,
            @JsonProperty("locationId") HistoryOfFields<Long> locationId,
            @JsonProperty("status") HistoryOfFields<String> status,
            @JsonProperty("registeredUsersId") List<Long> registeredUsersId) {
        this.eventId = eventId;
        this.ownerId = ownerId;
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
        this.registeredUsersId = registeredUsersId;
    }
}
