package ru.yandex.practicum.api.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.api.generation.RandomBurgerGenerator;
import ru.yandex.practicum.api.pojo.OrdersRequest;
import ru.yandex.practicum.api.pojo.UserRequest;
import ru.yandex.practicum.api.steps.OrdersRestApiSteps;
import ru.yandex.practicum.api.steps.UserRestApiSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getRandomUserRequest;

public class GetUserOrderTest {
    private UserRestApiSteps userRequest;
    private OrdersRestApiSteps ordersRequest;
    private String token;

    @Before
    public void setUp() {
        token = null;
        userRequest = new UserRestApiSteps();
        ordersRequest = new OrdersRestApiSteps();
        RandomBurgerGenerator randomBurger = new RandomBurgerGenerator();

        UserRequest randomUserRequest = getRandomUserRequest();
        OrdersRequest randomOrderRequest = randomBurger.generateRandomBurger();
        ValidatableResponse userResponse = userRequest.createUser(randomUserRequest);
        userResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = ordersRequest.createOrderWithIngredientsAndAuth(randomOrderRequest, token);
        orderResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userRequest.deleteUser(token).assertThat().statusCode(SC_ACCEPTED).and().body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Получение информации о заказах авторизованного пользователя")
    public void getUserOrdersWithAuthShouldBeReturnOrdersTest() {
        ValidatableResponse orderResponse = ordersRequest.getOrdersOfClientWithAuth(token);
        orderResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение информации о заказах неавторизованного пользователя")
    public void getUserOrdersWithoutAuthShouldBeRejectedTest() {
        ValidatableResponse orderResponse = ordersRequest.getOrdersOfClientWithoutAuth();
        orderResponse.assertThat().statusCode(SC_UNAUTHORIZED).and().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"));
    }
}
