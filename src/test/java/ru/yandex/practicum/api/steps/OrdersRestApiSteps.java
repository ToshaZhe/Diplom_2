package ru.yandex.practicum.api.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.api.pojo.OrdersRequest;

import static io.restassured.RestAssured.given;

public class OrdersRestApiSteps extends StellarBurgersRestApiSteps {
    private static final String BASE_PATH_INGREDIENTS = "ingredients";
    private static final String BASE_PATH_ORDERS = "orders";


    @Step("Получение списка всех ингредиентов")
    public ValidatableResponse getIngredientsList() {
        return given().spec(getDefaultRequestSpec()).get(BASE_PATH_INGREDIENTS).then();
    }


    @Step("Создание заказа с ингридиентами и без авторизации")
    public ValidatableResponse createOrderWithIngredientsWithoutAuth(OrdersRequest ordersRequest) {
        return given().spec(getDefaultRequestSpec()).body(ordersRequest).post(BASE_PATH_ORDERS).then();
    }

    @Step("Создание заказа с ингридиентами и авторизацией")
    public ValidatableResponse createOrderWithIngredientsAndAuth(OrdersRequest ordersRequest, String token) {
        return given().spec(getDefaultRequestSpec()).header("Authorization", token).body(ordersRequest)
                .post(BASE_PATH_ORDERS).then();
    }

    @Step("Создание заказа без ингридиентов")
    public ValidatableResponse createOrderWithoutIngredients() {
        return given().spec(getDefaultRequestSpec()).post(BASE_PATH_ORDERS).then();
    }

    @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getOrdersOfClientWithAuth(String token) {
        return given().spec(getDefaultRequestSpec()).header("Authorization", token)
                .get(BASE_PATH_ORDERS).then();
    }

    @Step("Получение заказов не авторизованного пользователя")
    public ValidatableResponse getOrdersOfClientWithoutAuth() {
        return given().spec(getDefaultRequestSpec()).get(BASE_PATH_ORDERS).then();
    }
}
