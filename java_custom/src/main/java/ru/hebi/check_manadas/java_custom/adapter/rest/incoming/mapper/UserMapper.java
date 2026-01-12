package ru.hebi.check_manadas.java_custom.adapter.rest.incoming.mapper;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import ru.hebi.check_manadas.java_custom.adapter.rest.incoming.dto.UserDto;
import ru.hebi.check_manadas.java_custom.domain.Result;
import ru.hebi.check_manadas.java_custom.domain.User;
import ru.hebi.check_manadas.java_custom.domain.error.Error;

/**
 * Преобразование сущности {@link User пользователь} из DTO
 */
@Component
public class UserMapper {

    /**
     * Получение сущности {@link User пользователь} из DTO
     *
     * @param userDto DTO
     * @return преобразованная сущность
     */
    @NonNull
    public Result<Error, User> toDomain(@NonNull UserDto userDto) {
        return Result.ok(new User(
                null,
                userDto.login(),
                userDto.email(),
                userDto.password()
        ));
    }

}
