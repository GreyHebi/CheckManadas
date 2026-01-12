package ru.hebi.check_manadas.java_custom.adapter.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hebi.check_manadas.java_custom.domain.User;

import java.util.Optional;

/**
 * Репозиторий для помощи работы с сущностью {@link User пользователь}
 */
public interface UserRepository
        extends JpaRepository<User, Long> {

    /**
     * Поиск пользователя по логину или адресу электронной почты
     *
     * @param login логин пользователя
     * @param email адрес электронной почты пользователя
     * @return пользователь с искомым логином или электронной почтой
     */
    Optional<User> findByLoginOrEmail(String login, String email);

}