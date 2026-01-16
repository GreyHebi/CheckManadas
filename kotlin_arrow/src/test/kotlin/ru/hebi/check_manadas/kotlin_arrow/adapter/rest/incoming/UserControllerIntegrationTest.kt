package ru.hebi.check_manadas.kotlin_arrow.adapter.rest.incoming

import lombok.SneakyThrows
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.matchesRegex
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.json.JsonCompareMode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.hebi.check_manadas.kotlin_arrow.adapter.db.UserRepository
import ru.hebi.check_manadas.kotlin_arrow.domain.User

private const val ROOT = "/users"

@Tag("integration")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var webMvc: MockMvc

    @BeforeEach
    fun beforeEach() {
        userRepository.deleteAllInBatch()
    }

    companion object {
        @JvmStatic
        @Container
        val postres: PostgreSQLContainer<*> =
            PostgreSQLContainer("postgres:15.3") //fixme установить ту же версию, что и в docker-compose
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass")

        @JvmStatic
        @DynamicPropertySource
        fun configurePostgresProperty(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url")      { postres.getJdbcUrl() }
            registry.add("spring.datasource.username") { postres.username }
            registry.add("spring.datasource.password") { postres.password }
        }
    }

    @DisplayName("Создание нового пользователя. Ошибка: Плохой запрос")
    @Test
    @SneakyThrows
    fun createNewUser_fail_badRequest() {
        val request = """
                {
                  "login": "",
                  "email": "",
                  "password": ""
                }""".trimIndent()

        webMvc.perform(
            post(ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("code").value("ERROR_CODE_02"))
            .andExpect(jsonPath("message").value(containsString("Логин не может быть пустым")))
            .andExpect(jsonPath("message").value(containsString("Адрес электронной почты не может быть пустым")))
            .andExpect(jsonPath("message").value(containsString("Пароль не может быть пустым")))
    }


    @DisplayName("Создание нового пользователя. Ошибка: Есть пользователь с таким же логином")
    @Test
    @SneakyThrows
    fun createNewUser_fail_duplicateLogin() {
        val login = "some_login"
        userRepository.save<User?>(User(null, login, "some@test.ru", "12345678"))

        val request = """
                {
                  "login": "$login",
                  "email": "test@test.ru",
                  "password": "qwerty"
                }"""
        val expectedResponse = """
                {
                "code": "ERROR_CODE_03",
                "message": "Логин 'some_login' занят"
                }""".trimIndent()

        webMvc.perform(
            post(ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse, JsonCompareMode.LENIENT))
    }

    @DisplayName("Создание нового пользователя. Ошибка: Есть пользователь с таким же адресом электронной почты")
    @Test
    @SneakyThrows
    fun createNewUser_fail_duplicateEmail() {
        val email = "test@test.ru"
        userRepository.save<User?>(User(null, "another", email, "12345678"))

        val request = """
                {
                  "login": "login",
                  "email": "$email",
                  "password": "qwerty"
                }"""
        val expectedResponse = """
                {
                "code": "ERROR_CODE_04",
                "message": "Адрес электронной почты 'test@test.ru' занят"
                }""".trimIndent()

        webMvc.perform(
            post(ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().json(expectedResponse, JsonCompareMode.LENIENT))
    }


    @DisplayName("Создание нового пользователя. Успешно")
    @Test
    @SneakyThrows
    fun createNewUser_success() {
        val request = """
                {
                  "login": "good_login",
                  "email": "good_mail@test.ru",
                  "password": "z1x2c3v4b5n6m7"
                }""".trimIndent()

        webMvc.perform(
            post(ROOT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(matchesRegex("\\d+")))
    }


}