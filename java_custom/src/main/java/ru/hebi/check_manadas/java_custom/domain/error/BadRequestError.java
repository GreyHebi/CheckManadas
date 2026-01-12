package ru.hebi.check_manadas.java_custom.domain.error;

import org.jspecify.annotations.NonNull;

/**
 * Ошибка "плохой запрос"
 * <p>
 * Используется при получении некорректных входных данных
 *
 * @param message описание ошибки
 */
public record BadRequestError(
        @NonNull String message
) implements Error {
}
