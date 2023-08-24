package ru.yandex.practicum.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.api.pojo.UserRequest;

import static io.restassured.RestAssured.given;

public class UserRestApiSteps extends StellarBurgersRestApiSteps {
    private static final String BASE_PATH_REGISTER = "auth/register";
    private static final String BASE_PATH_LOGIN = "auth/login";
    private static final String BASE_PATH_USER = "auth/user";
    private String token;

    @Step("Создание клиента. Email: {userRequest.email} Name: {userRequest.name}")
    public ValidatableResponse createUser(UserRequest userRequest) {
        return given().spec(getDefaultRequestSpec()).body(userRequest).post(BASE_PATH_REGISTER).then();
    }

    @Step("Авторизация клиента. Email: {userRequest.email}")
    public ValidatableResponse loginUser(UserRequest userRequest) {
        return given().spec(getDefaultRequestSpec()).body(userRequest).post(BASE_PATH_LOGIN).then();
    }

    @Step("Удаление пользователя. Token: {token}")
    public ValidatableResponse deleteUser(String token) {
        return given().spec(getDefaultRequestSpec()).header("Authorization", token).delete(BASE_PATH_USER).then();
    }

    @Step("Изменение данных клиента с токеном")
    public ValidatableResponse updateUserWithToken(UserRequest user, String token) {
        return given().spec(getDefaultRequestSpec()).header("Authorization", token).body(user).
                patch(BASE_PATH_USER).then();
    }

    @Step("Изменение данных клиента без токена")
    public ValidatableResponse updateUserWithoutToken(UserRequest user) {
        return given().spec(getDefaultRequestSpec()).body(user).patch(BASE_PATH_USER).then();
    }
}
