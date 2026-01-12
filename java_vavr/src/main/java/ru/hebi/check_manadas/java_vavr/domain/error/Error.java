package ru.hebi.check_manadas.java_vavr.domain.error;

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
