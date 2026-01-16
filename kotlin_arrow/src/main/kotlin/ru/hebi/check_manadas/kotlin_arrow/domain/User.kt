package ru.hebi.check_manadas.kotlin_arrow.domain

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Данные о пользователе
 */
@Entity
@Table(name = "application_user")
class User (
    /** Идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    /** Логин  */
    @Column(name = "login", nullable = false, unique = true, length = 64)
    var login: @NotBlank String? = null,
    /** Адрес электронной почты  */
    @Column(name = "email", nullable = false, unique = true)
    var email: @Email String? = null,
    /** Пароль  */
    @Column(name = "password", nullable = false)
    var password: @NotBlank String? = null,
) {
    override fun toString(): String {
        return "User{id=$id;login=$login}"
    }
}
