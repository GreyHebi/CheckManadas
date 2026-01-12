package ru.hebi.check_manadas.java_custom.adapter.rest.incoming.dto;

/**
 * Модель ошибки
 *
 * @param code    код ошибки
 * @param message описание ошибки
 */
public record ErrorDto(
        String code,
        String message
) {
}
