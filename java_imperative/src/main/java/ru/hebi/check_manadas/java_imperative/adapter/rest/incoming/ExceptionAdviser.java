package ru.hebi.check_manadas.java_imperative.adapter.rest.incoming;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hebi.check_manadas.java_imperative.adapter.rest.incoming.dto.ErrorDto;
import ru.hebi.check_manadas.java_imperative.domain.exception.DuplicateEmailException;
import ru.hebi.check_manadas.java_imperative.domain.exception.DuplicateLoginException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Exception exception) {
        log.error("Произошла непредвиденная ошибка", exception);
        return new ErrorDto(
                "ERROR_CODE_01",
                "Произошла непредвиденная ошибка"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleException(MethodArgumentNotValidException exception) {
        var errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        log.error("Пришел запрос с ошибками:\n{}", errorMessage);
        return new ErrorDto(
                "ERROR_CODE_02",
                errorMessage
        );
    }

    @ExceptionHandler(DuplicateLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleException(DuplicateLoginException exception) {
        log.error("", exception);
        return new ErrorDto(
                "ERROR_CODE_03",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleException(DuplicateEmailException exception) {
        log.error("", exception);
        return new ErrorDto(
                "ERROR_CODE_04",
                exception.getMessage()
        );
    }


}
