package ru.hebi.check_manadas.java_imperative.app.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.hebi.check_manadas.java_imperative.adapter.db.repository.UserRepository;
import ru.hebi.check_manadas.java_imperative.app.api.CreateNewUserInbound;
import ru.hebi.check_manadas.java_imperative.domain.User;
import ru.hebi.check_manadas.java_imperative.domain.exception.DuplicateEmailException;
import ru.hebi.check_manadas.java_imperative.domain.exception.DuplicateLoginException;

@RequiredArgsConstructor
@Slf4j
@Service
public class CreateNewUserUseCase
        implements CreateNewUserInbound {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public Long execute(@NonNull User user) {
        log.info("Создание нового пользователя с логином: {}", user.getLogin());
        checkDuplicate(user);
        var saved = userRepository.save(user);
        log.info("Новый пользователь сохранен: {}", saved);
        return saved.getId();
    }

    private void checkDuplicate(User user) {
        var login = user.getLogin();
        var email = user.getEmail();
        var duplicate = userRepository.findByLoginOrEmail(login, email);
        if (duplicate.isPresent()) {
            var entity = duplicate.get();
            if (login.equals(entity.getLogin())) {
                throw new DuplicateLoginException(login);
            } else {
                throw new DuplicateEmailException(email);
            }
        }
    }

}
