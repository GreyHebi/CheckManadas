package ru.hebi.check_manadas.java_vavr.domain.error;

import org.jspecify.annotations.NonNull;

/**
 * Ошибка при взаимодействии с базой данных
 *
 * @param message описание ошибки
 */
public record DatabaseCommunicationError(
        @NonNull String message
) implements Error {

    public static DatabaseCommunicationError of(Throwable cause) {
        return new DatabaseCommunicationError(
                cause.getMessage()
        );
    }

}
