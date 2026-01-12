package ru.hebi.check_manadas.java_vavr.adapter.rest.incoming.mapper;

import io.vavr.control.Either;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import ru.hebi.check_manadas.java_vavr.adapter.rest.incoming.dto.UserDto;
import ru.hebi.check_manadas.java_vavr.domain.User;
import ru.hebi.check_manadas.java_vavr.domain.error.Error;

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
    public Either<Error, User> toDomain(@NonNull UserDto userDto) {
        return Either.right(
                new User(
                        null,
                        userDto.login(),
                        userDto.email(),
                        userDto.password()
                )
        );
    }

}
