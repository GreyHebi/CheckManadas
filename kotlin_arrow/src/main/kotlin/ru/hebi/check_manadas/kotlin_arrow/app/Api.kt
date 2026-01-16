package ru.hebi.check_manadas.kotlin_arrow.app

import arrow.core.Either
import ru.hebi.check_manadas.kotlin_arrow.domain.Error
import ru.hebi.check_manadas.kotlin_arrow.domain.User

/**
 * Создание и сохранение нового пользователя
 */
interface CreateNewUserInbound {
    /**
     * Создание и сохранение нового пользователя
     *
     * @param user данные по новому пользователю
     * @return идентификатор нового сохраненного пользователя
     */
    fun execute(user: User): Either<Error, Long>
}