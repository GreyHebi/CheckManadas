package ru.hebi.check_manadas.java_custom.adapter.rest.incoming;

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
import ru.hebi.check_manadas.java_custom.adapter.rest.incoming.dto.UserDto;
import ru.hebi.check_manadas.java_custom.adapter.rest.incoming.mapper.UserMapper;
import ru.hebi.check_manadas.java_custom.app.api.CreateNewUserInbound;
import ru.hebi.check_manadas.java_custom.domain.Result;
import ru.hebi.check_manadas.java_custom.domain.error.BadRequestError;

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
                .map(userMapper::toDomain)
                .map(createNewUserInbound::execute)
                .apply(
                        ExceptionAdviser::handle,
                        result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
                );
    }

    @NonNull
    private <T> Result<BadRequestError, T> validate(T body) {
        var result = validator.validate(body);
        if (result.isEmpty())
            return Result.ok(body);

        var message = result.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("\n"));
        return Result.error(new BadRequestError(message));
    }

}
