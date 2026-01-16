package ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val code: String,
    val message: String
)
