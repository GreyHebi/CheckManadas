package ru.hebi.check_manadas.java_custom.domain.error;

import org.jspecify.annotations.NonNull;

/**
 * Ошибка при применении функции
 */
public interface Error {

    /**
     * @return описание ошибки
     */
    @NonNull String message();

}
