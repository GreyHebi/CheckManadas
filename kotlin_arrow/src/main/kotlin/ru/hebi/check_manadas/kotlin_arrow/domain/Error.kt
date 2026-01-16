package ru.hebi.check_manadas.kotlin_arrow.domain

/**
 * Ошибка при применении функции
 */
interface Error {
    /**
     * @return описание ошибки
     */
    fun message(): String
}


/**
 * Ошибка "плохой запрос"
 *
 * Используется при получении некорректных входных данных
 */
class BadRequestError(
    /** Описание ошибки */
    private val message: String
) : Error {
    override fun message() = message
}


/**
 * Ошибка при взаимодействии с базой данных
 */
class DatabaseCommunicationError(
    /** Описание ошибки */
    private val message: String
) : Error {
    override fun message() = message
}
fun databaseCommunicationError(error: Throwable) = DatabaseCommunicationError(error.message ?: "")


/**
 * Ошибка дублирования адреса электронной почты пользователя
 */
class DuplicateEmailError(
    /** Дубликат электронной почты */
    private val email: String
) : Error {
    override fun message() = "Адрес электронной почты '$email' занят"
}


/**
 * Ошибка дублирования логина пользователя
 */
class DuplicateLoginError(
    /** Дубликат логина */
    private val login: String
) : Error {
    override fun message() = "Логин '$login' занят"
}