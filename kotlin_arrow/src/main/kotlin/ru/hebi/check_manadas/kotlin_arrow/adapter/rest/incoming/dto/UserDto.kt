package ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable
import org.hibernate.validator.constraints.Length

/**
 * DTO для [ru.hebi.check_manadas.kotlin_arrow.domain.User]
 */
@Serializable
data class UserDto(
    @field:NotBlank(message = "Логин не может быть пустым")
    @field:Length(message = "Логин должен быть длиной от 1 до 64")
    val login: String,
    @field:NotBlank(message = "Адрес электронной почты не может быть пустым")
    @field:Email(message = "Не корректный формат электронной почты")
    val email: String,
    @field:NotBlank(message = "Пароль не может быть пустым")
    val password: String,
)