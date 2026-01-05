package ru.hebi.check_manadas.java_imperative.domain.exception;

public class DuplicateLoginException
        extends RuntimeException {

    public DuplicateLoginException(String login) {
        super("Логин '%s' занят".formatted(login));
    }
}
