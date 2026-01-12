package ru.hebi.check_manadas.java_vavr.adapter.rest.incoming;

import io.vavr.control.Either;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hebi.check_manadas.java_vavr.adapter.rest.incoming.dto.UserDto;
import ru.hebi.check_manadas.java_vavr.adapter.rest.incoming.mapper.UserMapper;
import ru.hebi.check_manadas.java_vavr.app.api.CreateNewUserInbound;
import ru.hebi.check_manadas.java_vavr.domain.error.BadRequestError;
import ru.hebi.check_manadas.java_vavr.domain.error.Error;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateNewUserInbound createNewUserInbound;
    private final UserMapper userMapper;
    private final Validator validator;


    @PostMapping
    public ResponseEntity<?> createNewUser(
            @RequestBody UserDto newUser
    ) {
        return validate(newUser)
                .flatMap(userMapper::toDomain)
                .flatMap(createNewUserInbound::execute)
                .fold(
                        ExceptionAdviser::handle,
                        result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
                );
    }

    @NonNull
    private <T> Either<Error, T> validate(T body) {
        var result = validator.validate(body);
        return Either.cond(
                result.isEmpty(),
                //успешный ответ
                () -> body,
                //ошибка
                () -> {
                    var message = result.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("\n"));
                    return new BadRequestError(message);
                }
        );
    }


}
