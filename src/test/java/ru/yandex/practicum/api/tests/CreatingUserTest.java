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
import static ru.yandex.practicum.api.generation.UserRequestGenerator.*;

public class CreatingUserTest {
    private UserRestApiSteps userRequest;
    private String token;

    @Before
    public void setUp() {
        token = null;
        userRequest = new UserRestApiSteps();
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userRequest.deleteUser(token).assertThat().statusCode(SC_ACCEPTED).and().body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Создание курьера, получение token")
    public void userCreatingCorrectParamShouldBeCreatedTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        ValidatableResponse response = userRequest.createUser(randomUserRequest);
        response.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание зарегистрированного пользователя")
    public void userCreatingNotUniqueEmailShouldNotBeCreatedTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        ValidatableResponse response = userRequest.createUser(randomUserRequest);
        response.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = response.extract().path("accessToken");

        userRequest.createUser(randomUserRequest).assertThat().statusCode(SC_FORBIDDEN).
                and().body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым email")
    public void userCreatingEmailIsNullShouldNotBeCreatedTest() {
        UserRequest badUserRequest = getUserRequestWithoutEmail();
        userRequest.createUser(badUserRequest).assertThat().statusCode(SC_FORBIDDEN).
                and().body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя с пустым password")
    public void userCreatingPasswordIsNullShouldNotBeCreatedTest() {
        UserRequest badUserRequest = getUserRequestWithoutPassword();
        userRequest.createUser(badUserRequest).assertThat().statusCode(SC_FORBIDDEN).
                and().body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Создание пользователя с пустым name")
    public void userCreatingNameIsNullShouldNotBeCreatedTest() {
        UserRequest badUserRequest = getUserRequestWithoutName();
        userRequest.createUser(badUserRequest).assertThat().statusCode(SC_FORBIDDEN).
                and().body("message", equalTo("Email, password and name are required fields"));

    }
}
