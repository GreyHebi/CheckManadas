package ru.hebi.check_manadas.java_imperative.adapter.rest.incoming.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import ru.hebi.check_manadas.java_imperative.domain.User;

/**
 * DTO для {@link User}
 */
public record UserDto(
        @NotBlank(message = "Логин не может быть пустым")
        @Length(message = "Логин должен быть длиной от 1 до 64")
        String login,
        @NotBlank(message = "Адрес электронной почты не может быть пустым")
        @Email(message = "Не корректный формат электронной почты")
        String email,
        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {
}