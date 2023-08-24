package ru.yandex.practicum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.api.pojo.UserRequest;
import ru.yandex.practicum.api.steps.UserRestApiSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getRandomUserRequest;

public class LogonUserTest {
    private UserRestApiSteps userRequest;
    private UserRequest randomUserRequest;
    private String token;

    @Before
    public void setUp() {
        token = null;
        userRequest = new UserRestApiSteps();
        randomUserRequest = getRandomUserRequest();
        ValidatableResponse response = userRequest.createUser(randomUserRequest);
        response.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = response.extract().path("accessToken");
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userRequest.deleteUser(token).assertThat().statusCode(SC_ACCEPTED).and().body("success", equalTo(true));
        }
    }

    @DisplayName("Авторизация пользователя с корректными параметрами")
    @Test
    public void userAuthCorrectParamShouldBeAuthorizedTest() {
        ValidatableResponse loginResponse = userRequest.loginUser(randomUserRequest);
        loginResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = loginResponse.extract().path("accessToken");
    }

    @DisplayName("Авторизация пользователя с пустым email")
    @Test
    public void userAuthEmailIsEmptyShouldNotBeAuthorizedTest() {
        randomUserRequest.setEmail("");
        ValidatableResponse loginResponse = userRequest.loginUser(randomUserRequest);
        loginResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().
                body("message", equalTo("email or password are incorrect"));
    }

    @DisplayName("Авторизация пользователя с пустым паролем")
    @Test
    public void userAuthPasswordIsEmptyShouldNotBeAuthorizedTest() {
        randomUserRequest.setPassword("");
        ValidatableResponse loginResponse = userRequest.loginUser(randomUserRequest);
        loginResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().
                body("message", equalTo("email or password are incorrect"));
    }

    @DisplayName("Авторизация пользователя с неверным email")
    @Test
    public void userAuthIncorrectEmailShouldNotBeAuthorizedTest() {
        randomUserRequest.setEmail("qwerty@ytre.wq");
        ValidatableResponse loginResponse = userRequest.loginUser(randomUserRequest);
        loginResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().
                body("message", equalTo("email or password are incorrect"));
    }

    @DisplayName("Авторизация пользователя c неверным паролем")
    @Test
    public void userAuthIncorrectPasswordShouldNotBeAuthorizedTest() {
        randomUserRequest.setPassword("ytrewq");
        ValidatableResponse loginResponse = userRequest.loginUser(randomUserRequest);
        loginResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().
                body("message", equalTo("email or password are incorrect"));
    }
}
