package ru.gorohov;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data

public class HistoryOfFields<T> {
    private T oldField;
    private T newField;
    @JsonCreator
    public HistoryOfFields(@JsonProperty("oldField") T oldField, @JsonProperty("newField") T newField) {
        this.oldField = oldField;
        this.newField = newField;
    }
}
