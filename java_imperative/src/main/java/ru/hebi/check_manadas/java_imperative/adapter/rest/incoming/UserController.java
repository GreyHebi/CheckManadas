package ru.hebi.check_manadas.java_imperative.adapter.rest.incoming;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.hebi.check_manadas.java_imperative.adapter.rest.incoming.dto.UserDto;
import ru.hebi.check_manadas.java_imperative.adapter.rest.incoming.mapper.UserMapper;
import ru.hebi.check_manadas.java_imperative.app.api.CreateNewUserInbound;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateNewUserInbound createNewUserInbound;
    private final UserMapper userMapper;


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long createNewUser(
            @RequestBody @Valid UserDto newUser
    ) {
        var domain = userMapper.toDomain(newUser);
        return createNewUserInbound.execute(domain);
    }

}

