package ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming

import arrow.core.flatMap
import arrow.core.raise.either
import arrow.core.raise.ensure
import jakarta.validation.Validator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.dto.UserDto
import ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.mapper.toDomain
import ru.hebi.check_manadas.kotlin_arrow.app.CreateNewUserInbound
import ru.hebi.check_manadas.kotlin_arrow.domain.BadRequestError
import java.util.stream.Collectors

private fun <T> Validator.check(body: T) = either {
    val result = validate(body)
    ensure(result.isEmpty()) {
        val message = result.stream()
            .map { it.message }
            .collect(Collectors.joining("\n"))
        BadRequestError(message)
    }
    return@either body
}


@RestController
@RequestMapping("/users")
class UserController(
    private val createNewUserInbound: CreateNewUserInbound,
    private val validator: Validator
) {

    @PostMapping
    fun createNewUser(@RequestBody userDto: UserDto) = validator.check(userDto)
        .map { toDomain(it) }
        .flatMap { createNewUserInbound.execute(it) }
        .fold(
            { handle(it) },
            { ResponseEntity.status(HttpStatus.CREATED).body(it)}
        )
}