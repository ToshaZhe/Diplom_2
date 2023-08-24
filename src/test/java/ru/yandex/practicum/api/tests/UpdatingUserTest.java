package ru.yandex.practicum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.api.pojo.UserRequest;
import ru.yandex.practicum.api.steps.UserRestApiSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getRandomUserRequest;

public class UpdatingUserTest {
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

    @Test
    @DisplayName("Обновление email с авторизацией")
    public void userUpdatingEmailWithTokenShouldBeUpdatedTest() {
        randomUserRequest.setEmail(RandomStringUtils.randomAlphabetic(6) + "@ya.ru");
        ValidatableResponse updateResponse = userRequest.updateUserWithToken(randomUserRequest, token);
        updateResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление password с авторизацией")
    public void userUpdatingPasswordWithTokenShouldBeUpdatedTest() {
        randomUserRequest.setPassword("qwerty123456");
        ValidatableResponse updateResponse = userRequest.updateUserWithToken(randomUserRequest, token);
        updateResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление name с авторизацией")
    public void userUpdatingNameWithTokenShouldBeUpdatedTest() {
        randomUserRequest.setName("UpdatedName");
        ValidatableResponse updateResponse = userRequest.updateUserWithToken(randomUserRequest, token);
        updateResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Обновление email без авторизации")
    public void userUpdatingEmailWithoutTokenShouldBeRejectedTest() {
        randomUserRequest.setEmail("updated.email.redject@ya.ru");
        ValidatableResponse updateResponse = userRequest.updateUserWithoutToken(randomUserRequest);
        updateResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление password без авторизации")
    public void userUpdatingPasswordWithoutTokenShouldBeRejectedTest() {
        randomUserRequest.setPassword("qwerty123456");
        ValidatableResponse updateResponse = userRequest.updateUserWithoutToken(randomUserRequest);
        updateResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Обновление name без авторизации")
    public void userUpdatingNameWithoutTokenShouldBeRejectedTest() {
        randomUserRequest.setName("UpdatedName");
        ValidatableResponse updateResponse = userRequest.updateUserWithoutToken(randomUserRequest);
        updateResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().body("message", equalTo("You should be authorised"));
    }
}
