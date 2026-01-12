package ru.hebi.check_manadas.java_custom.app.api;

import org.jspecify.annotations.NonNull;
import ru.hebi.check_manadas.java_custom.domain.Result;
import ru.hebi.check_manadas.java_custom.domain.error.Error;
import ru.hebi.check_manadas.java_custom.domain.User;

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
    Result<Error, Long> execute(
            @NonNull User user
    );

}
