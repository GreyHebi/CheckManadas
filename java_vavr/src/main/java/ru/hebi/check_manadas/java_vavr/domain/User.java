package ru.hebi.check_manadas.java_vavr.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Данные о пользователе
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "login"})
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "application_user")
public class User {
    /** Идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    /** Логин */
    @NotBlank
    @Column(name = "login", nullable = false, unique = true, length = 64)
    private String login;
    /** Адрес электронной почты */
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    /** Пароль */
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
}