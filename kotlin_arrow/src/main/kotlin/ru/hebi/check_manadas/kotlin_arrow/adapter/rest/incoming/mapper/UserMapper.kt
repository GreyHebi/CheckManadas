package ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.mapper

import ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.dto.UserDto
import ru.hebi.check_manadas.kotlin_arrow.domain.User

fun toDomain(userDto: UserDto) = User(
    login = userDto.login,
    email = userDto.email,
    password = userDto.password
)