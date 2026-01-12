package ru.hebi.check_manadas.java_vavr.app.impl;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ru.hebi.check_manadas.java_vavr.adapter.db.repository.UserRepository;
import ru.hebi.check_manadas.java_vavr.app.api.CreateNewUserInbound;
import ru.hebi.check_manadas.java_vavr.domain.User;
import ru.hebi.check_manadas.java_vavr.domain.error.DatabaseCommunicationError;
import ru.hebi.check_manadas.java_vavr.domain.error.DuplicateEmailError;
import ru.hebi.check_manadas.java_vavr.domain.error.DuplicateLoginError;
import ru.hebi.check_manadas.java_vavr.domain.error.Error;

@RequiredArgsConstructor
@Slf4j
@Service
public class CreateNewUserUseCase
        implements CreateNewUserInbound {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public Either<Error, Long> execute(@NonNull User user) {
        log.info("Создание нового пользователя с логином: {}", user.getLogin());
        return checkDuplicate(user)
                .fold(
                        error -> Try.<User>failure(new ErrorWrapper(error)),
                        Try::success
                )
                .mapTry(newUser -> {
                    var saved = userRepository.save(newUser);
                    log.info("Новый пользователь сохранен: {}", saved);
                    return saved.getId();
                })
                .toEither(
                        error -> error instanceof ErrorWrapper ew
                                ? ew.error
                                : databaseCommunicationError("сохранение нового пользователя", error)
                );
    }

    /**
     * Проверка наличия дублирования {@link User#getLogin() логина} или {@link User#getEmail() адреса электронной почты}
     *
     * @param user новый пользователь
     * @return либо этот же пользователь,
     * либо ошибка дублирования ({@link DuplicateLoginError} или {@link DuplicateEmailError})
     */
    private Either<Error, User> checkDuplicate(final User user) {
        var login = user.getLogin();
        var email = user.getEmail();

        return Try.of(() -> userRepository.findByLoginOrEmail(login, email))
                .map(entityOpt -> Option.ofOptional(entityOpt)
                        .toEither(user)
                        .map(entity -> {
                            if (login.equals(entity.getLogin())) {
                                log.error("Найден дубликат логина {}", login);
                                return new DuplicateLoginError(login);
                            } else {
                                log.error("Найден дубликат адреса электронной почты {}", email);
                                return (Error) new DuplicateEmailError(email);
                            }
                        })
                        .swap()
                )
                .getOrElseGet(
                        error -> Either.left(databaseCommunicationError("поиск дубликатов", error))
                );
    }

    @NonNull
    private static Error databaseCommunicationError(String taskName, Throwable error) {
        log.error("При '{}' произошла ошибка", taskName, error);
        return DatabaseCommunicationError.of(error);
    }

    @AllArgsConstructor
    private static final class ErrorWrapper extends RuntimeException {
        private final Error error;
    }

}
