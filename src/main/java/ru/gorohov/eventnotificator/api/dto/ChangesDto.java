package ru.gorohov.eventnotificator.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangesDto {

    private String fieldName;

    private String oldValue;

    private String newValue;
}
