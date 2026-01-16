package ru.hebi.check_manadas.kotlin_arrow.adapter.db

import org.springframework.data.jpa.repository.JpaRepository
import ru.hebi.check_manadas.kotlin_arrow.domain.User

/**
 * Репозиторий для помощи работы с сущностью [User]
 */
interface UserRepository : JpaRepository<User, Long> {
    /**
     * Поиск пользователя по логину или адресу электронной почты
     *
     * @param login логин пользователя
     * @param email адрес электронной почты пользователя
     * @return пользователь с искомым логином или электронной почтой
     */
    fun findByLoginOrEmail(login: String, email: String): User?
}