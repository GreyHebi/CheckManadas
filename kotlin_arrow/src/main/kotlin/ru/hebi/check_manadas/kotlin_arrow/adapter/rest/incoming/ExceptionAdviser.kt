package ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming

import org.springframework.http.ResponseEntity
import ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.dto.ErrorDto
import ru.hebi.check_manadas.kotlin_arrow.domain.BadRequestError
import ru.hebi.check_manadas.kotlin_arrow.domain.DuplicateEmailError
import ru.hebi.check_manadas.kotlin_arrow.domain.DuplicateLoginError
import ru.hebi.check_manadas.kotlin_arrow.domain.Error

fun handle(error: Error): ResponseEntity<ErrorDto> = when (error) {
    is BadRequestError -> ResponseEntity
        .badRequest()
        .body(ErrorDto("ERROR_CODE_02", error.message()))

    is DuplicateLoginError -> ResponseEntity
        .badRequest()
        .body(ErrorDto("ERROR_CODE_03", error.message()))

    is DuplicateEmailError -> ResponseEntity
        .badRequest()
        .body(ErrorDto("ERROR_CODE_04", error.message()))

    else -> ResponseEntity
        .internalServerError()
        .body(ErrorDto("ERROR_CODE_01", error.message()))
}
