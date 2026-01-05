package ru.hebi.check_manadas.java_imperative.adapter.rest.incoming;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.hebi.check_manadas.java_imperative.adapter.db.repository.UserRepository;
import ru.hebi.check_manadas.java_imperative.domain.User;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserControllerIntegrationTest {

    private static final String ROOT = "/users";

    @Container
    static PostgreSQLContainer<?> postres = new PostgreSQLContainer<>("postgres:15.3") //fixme установить ту же версию, что и в docker-compose
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired private UserRepository userRepository;
    @Autowired private MockMvc webMvc;


    @DynamicPropertySource
    static void configurePostgresProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postres::getJdbcUrl);
        registry.add("spring.datasource.username", postres::getUsername);
        registry.add("spring.datasource.password", postres::getPassword);
    }

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("Создание нового пользователя. Ошибка: Плохой запрос")
    @Test
    @SneakyThrows
    void createNewUser_fail_badRequest() {
        var request = """
                {
                  "login": "",
                  "email": "",
                  "password": ""
                }""";

        webMvc.perform(
                        post(ROOT)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("ERROR_CODE_02"))
                .andExpect(jsonPath("message").value(containsString("Логин не может быть пустым")))
                .andExpect(jsonPath("message").value(containsString("Адрес электронной почты не может быть пустым")))
                .andExpect(jsonPath("message").value(containsString("Пароль не может быть пустым")));
    }


    @DisplayName("Создание нового пользователя. Ошибка: Есть пользователь с таким же логином")
    @Test
    @SneakyThrows
    void createNewUser_fail_duplicateLogin() {
        var login = "some_login";
        userRepository.save(new User(null, login, "some@test.ru", "12345678"));

        var request = """
                {
                  "login": "%s",
                  "email": "test@test.ru",
                  "password": "qwerty"
                }""".formatted(login);
        var expectedResponse = """
                {
                "code": "ERROR_CODE_03",
                "message": "Логин 'some_login' занят"
                }
                """;

        webMvc.perform(
                        post(ROOT)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, JsonCompareMode.LENIENT));
    }

    @DisplayName("Создание нового пользователя. Ошибка: Есть пользователь с таким же адресом электронной почты")
    @Test
    @SneakyThrows
    void createNewUser_fail_duplicateEmail() {
        var email = "test@test.ru";
        userRepository.save(new User(null, "another", email, "12345678"));

        var request = """
                {
                  "login": "login",
                  "email": "%s",
                  "password": "qwerty"
                }""".formatted(email);
        var expectedResponse = """
                {
                "code": "ERROR_CODE_04",
                "message": "Адрес электронной почты 'test@test.ru' занят"
                }
                """;

        webMvc.perform(
                        post(ROOT)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse, JsonCompareMode.LENIENT));
    }


    @DisplayName("Создание нового пользователя. Успешно")
    @Test
    @SneakyThrows
    void createNewUser_success() {
        var request = """
                {
                  "login": "good_login",
                  "email": "good_mail@test.ru",
                  "password": "z1x2c3v4b5n6m7"
                }""";

        webMvc.perform(
                        post(ROOT)
                                .contentType(APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(matchesRegex("\\d+")));
    }
}