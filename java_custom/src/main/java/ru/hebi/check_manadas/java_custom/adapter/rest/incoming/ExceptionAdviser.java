package ru.hebi.check_manadas.java_custom.adapter.rest.incoming;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;
import ru.hebi.check_manadas.java_custom.adapter.rest.incoming.dto.ErrorDto;
import ru.hebi.check_manadas.java_custom.domain.error.BadRequestError;
import ru.hebi.check_manadas.java_custom.domain.error.DuplicateEmailError;
import ru.hebi.check_manadas.java_custom.domain.error.DuplicateLoginError;
import ru.hebi.check_manadas.java_custom.domain.error.Error;

/**
 * Обработка ошибок применения функции
 */
@UtilityClass
public class ExceptionAdviser {

    public static ResponseEntity<ErrorDto> handle(Error error) {
        if (error instanceof BadRequestError e)
            return ResponseEntity.badRequest().body(
                    new ErrorDto(
                            "ERROR_CODE_02",
                            e.message()
                    )
            );

        else if (error instanceof DuplicateLoginError e)
            return ResponseEntity.badRequest().body(
                    new ErrorDto(
                            "ERROR_CODE_03",
                            e.message()
                    )
            );

        else if (error instanceof DuplicateEmailError e)
            return ResponseEntity.badRequest().body(
                    new ErrorDto(
                            "ERROR_CODE_04",
                            e.message()
                    )
            );

        else
            return ResponseEntity.internalServerError().body(
                    new ErrorDto(
                            "ERROR_CODE_01",
                            "Произошла непредвиденная ошибка: " + error.message()
                    )
            );
    }

}
