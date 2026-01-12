package ru.hebi.check_manadas.java_custom.domain.error;

import org.jspecify.annotations.NonNull;

/**
 * Ошибка дублирования логина пользователя
 *
 * @param login дубликат логина
 */
public record DuplicateLoginError(
        @NonNull String login
) implements Error {

    @NonNull
    public String message() {
        return "Логин '" + this.login + "' занят";
    }

}
