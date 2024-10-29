package ru.gorohov.eventnotificator.secured.user.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gorohov.eventnotificator.secured.user.domain.UserRole;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String login;
    private Integer age;
    private UserRole role;
}
