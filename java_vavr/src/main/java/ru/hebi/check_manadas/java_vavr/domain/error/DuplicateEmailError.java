package ru.hebi.check_manadas.java_vavr.domain.error;

import org.jspecify.annotations.NonNull;

/**
 * Ошибка дублирования адреса электронной почты пользователя
 *
 * @param email дубликат электронной почты
 */
public record DuplicateEmailError(
        @NonNull String email
) implements Error {

    @NonNull
    public String message() {
        return "Адрес электронной почты '" + this.email + "' занят";
    }

}
