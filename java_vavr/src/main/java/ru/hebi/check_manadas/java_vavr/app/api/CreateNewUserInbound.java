package ru.hebi.check_manadas.java_vavr.app.api;

import io.vavr.control.Either;
import org.jspecify.annotations.NonNull;
import ru.hebi.check_manadas.java_vavr.domain.User;
import ru.hebi.check_manadas.java_vavr.domain.error.Error;

/**
 * Создание и сохранение нового пользователя
 */
public interface CreateNewUserInbound {

    /**
     * Создание и сохранение нового пользователя
     *
     * @param user данные по новому пользователю
     * @return идентификатор нового сохраненного пользователя
     */
    @NonNull
    Either<Error, Long> execute(
            @NonNull User user
    );

}
