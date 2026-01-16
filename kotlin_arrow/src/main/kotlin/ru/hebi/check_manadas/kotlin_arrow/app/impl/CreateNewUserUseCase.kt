package ru.hebi.check_manadas.kotlin_arrow.app.impl

import arrow.core.Either
import arrow.core.Either.Companion.catch
import arrow.core.flatMap
import arrow.core.raise.either
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.hebi.check_manadas.kotlin_arrow.adapter.db.UserRepository
import ru.hebi.check_manadas.kotlin_arrow.app.CreateNewUserInbound
import ru.hebi.check_manadas.kotlin_arrow.domain.DuplicateEmailError
import ru.hebi.check_manadas.kotlin_arrow.domain.DuplicateLoginError
import ru.hebi.check_manadas.kotlin_arrow.domain.Error
import ru.hebi.check_manadas.kotlin_arrow.domain.User
import ru.hebi.check_manadas.kotlin_arrow.domain.databaseCommunicationError


private val log: Logger = LoggerFactory.getLogger(CreateNewUserUseCase::class.java)

@Service
class CreateNewUserUseCase(
    private val userRepository: UserRepository
) : CreateNewUserInbound {

    override fun execute(user: User): Either<Error, Long> {
        log.info("Создание нового пользователя с логином: {}", user.login)
        return checkDuplicate(user)
            .flatMap { save(it) }
    }

    private fun checkDuplicate(user: User) = either {
        val login = user.login!!
        val email = user.email!!
        catch {
            userRepository.findByLoginOrEmail(login, email)
        }.fold(
            { throwable -> raise(databaseCommunicationError("поиск дубликатов", throwable)) },
            { it?.let {
                    if (it.login == login)
                        raise(DuplicateLoginError(login))
                    else
                        raise(DuplicateEmailError(email))
            } }
        )
        user
    }

    private fun save(user: User) = catch {
        val saved = userRepository.save(user)
        log.info("Новый пользователь сохранен: {}", saved)
        saved.id!!
    }.mapLeft { throwable ->
        databaseCommunicationError("сохранение нового пользователя", throwable)
    }

}


private fun databaseCommunicationError(taskName: String, error: Throwable) = databaseCommunicationError(error).also {
    log.error("При '{}' произошла ошибка", taskName, error)
}