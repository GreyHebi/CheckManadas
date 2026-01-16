package ru.hebi.check_manadas.kotlin_arrow.fw

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

@Configuration(proxyBeanMethods = false)
class SerializationConfig {

    @Bean fun kotlinSerializationJsonHttpMessageConverter() = KotlinSerializationJsonHttpMessageConverter(Json {
        isLenient = true
    })

}