package ru.hebi.check_manadas.java_custom.app.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.hebi.check_manadas.java_custom.adapter.db.repository.UserRepository;
import ru.hebi.check_manadas.java_custom.app.api.CreateNewUserInbound;
import ru.hebi.check_manadas.java_custom.domain.Result;
import ru.hebi.check_manadas.java_custom.domain.User;
import ru.hebi.check_manadas.java_custom.domain.error.DatabaseCommunicationError;
import ru.hebi.check_manadas.java_custom.domain.error.DuplicateEmailError;
import ru.hebi.check_manadas.java_custom.domain.error.DuplicateLoginError;
import ru.hebi.check_manadas.java_custom.domain.error.Error;

@RequiredArgsConstructor
@Slf4j
@Service
public class CreateNewUserUseCase
        implements CreateNewUserInbound {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public Result<Error, Long> execute(@NonNull User user) {
        log.info("Создание нового пользователя с логином: {}", user.getLogin());
        return checkDuplicate(user)
                .map(
                        error -> databaseCommunicationError("сохранение нового пользователя", error),
                        us -> {
                            var saved = userRepository.save(us);
                            log.info("Новый пользователь сохранен: {}", saved);
                            return Result.ok(saved.getId());
                        }
                );
    }

    /**
     * Проверка наличия дублирования {@link User#getLogin() логина} или {@link User#getEmail() адреса электронной почты}
     *
     * @param user новый пользователь
     * @return либо этот же пользователь,
     * либо ошибка дублирования ({@link DuplicateLoginError} или {@link DuplicateEmailError})
     */
    private Result<Error, User> checkDuplicate(User user) {
        var login = user.getLogin();
        var email = user.getEmail();

        return Result.attempt(
                error -> databaseCommunicationError("поиск дубликатов", error),
                () -> Result.ok(userRepository.findByLoginOrEmail(login, email))
        ).map(
                duplicate -> {
                    if (duplicate.isEmpty()) return Result.ok(user);

                    var entity = duplicate.get();
                    if (login.equals(entity.getLogin())) {
                        log.error("Найден дубликат логина {}", login);
                        return Result.error(new DuplicateLoginError(login));
                    } else {
                        log.error("Найден дубликат адреса электронной почты {}", email);
                        return Result.error(new DuplicateEmailError(email));
                    }
                }
        );
    }

    private static <R> Result<Error, R> databaseCommunicationError(String taskName, Exception error) {
        log.error("При '{}' произошла ошибка", taskName, error);
        return Result.error(DatabaseCommunicationError.of(error));
    }

}
