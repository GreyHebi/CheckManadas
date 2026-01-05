package ru.hebi.check_manadas.java_imperative.domain.exception;

public class DuplicateEmailException
        extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Адрес электронной почты '%s' занят".formatted(email));
    }
}
