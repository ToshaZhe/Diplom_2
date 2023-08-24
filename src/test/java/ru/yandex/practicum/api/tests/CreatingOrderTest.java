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
import static ru.yandex.practicum.api.generation.RandomBurgerGenerator.generateBadBurger;
import static ru.yandex.practicum.api.generation.UserRequestGenerator.getRandomUserRequest;

public class CreatingOrderTest {
    private UserRestApiSteps userRequest;
    private OrdersRestApiSteps ordersRequest;
    private RandomBurgerGenerator randomBurger;
    private String token;

    @Before
    public void setUp() {
        token = null;
        userRequest = new UserRestApiSteps();
        ordersRequest = new OrdersRestApiSteps();
        randomBurger = new RandomBurgerGenerator();
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userRequest.deleteUser(token).assertThat().statusCode(SC_ACCEPTED).and().body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами авторизованным пользователем")
    public void orderCreatingWithAuthAndIngredientsShouldBeCreatedTest() {
        UserRequest randomUserRequest = getRandomUserRequest();
        OrdersRequest randomOrderRequest = randomBurger.generateRandomBurger();
        ValidatableResponse userResponse = userRequest.createUser(randomUserRequest);
        userResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        token = userResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = ordersRequest.createOrderWithIngredientsAndAuth(randomOrderRequest, token);
        orderResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами неавторизованным пользователем")
    public void orderCreatingWithNotAuthAndIngredientsShouldBeCreatedTest() {
        OrdersRequest randomOrderRequest = randomBurger.generateRandomBurger();
        ValidatableResponse orderResponse = ordersRequest.createOrderWithIngredientsWithoutAuth(randomOrderRequest);
        orderResponse.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void orderCreatingWithoutIngredientsShouldBeRejectedTest() {
        ValidatableResponse orderResponse = ordersRequest.createOrderWithoutIngredients();
        orderResponse.assertThat().statusCode(SC_BAD_REQUEST).and().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверными id ингридиентов")
    public void orderCreatingWithWrongIngredientsIdShouldBeRejectedTest() {
        OrdersRequest randomOrderRequest = generateBadBurger();
        ValidatableResponse orderResponse = ordersRequest.createOrderWithIngredientsWithoutAuth(randomOrderRequest);
        orderResponse.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
