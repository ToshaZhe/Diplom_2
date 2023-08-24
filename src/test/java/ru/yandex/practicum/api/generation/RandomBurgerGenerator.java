package ru.yandex.practicum.api.generation;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practicum.api.pojo.IngredientsData;
import ru.yandex.practicum.api.pojo.IngredientsResponse;
import ru.yandex.practicum.api.pojo.OrdersRequest;
import ru.yandex.practicum.api.steps.OrdersRestApiSteps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class RandomBurgerGenerator {

    public static OrdersRequest generateBadBurger() {
        OrdersRequest ordersRequest = new OrdersRequest();
        List<String> badBurger = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            badBurger.add(RandomStringUtils.randomAlphanumeric(24));
        }
        ordersRequest.setIngredients(badBurger);
        return ordersRequest;
    }

    public IngredientsResponse getAllIngredients() {
        OrdersRestApiSteps getAllIngredients = new OrdersRestApiSteps();
        ValidatableResponse response = getAllIngredients.getIngredientsList();
        response.assertThat().statusCode(SC_OK).and().body("success", equalTo(true));
        return response.extract().as(IngredientsResponse.class);
    }

    public List<String> getListOfBunsId(IngredientsResponse allIngredients) {
        List<String> listOfBunsId = new ArrayList<>();
        for (IngredientsData data : allIngredients.getData()) {
            if (data.getType().equals("bun")) {
                listOfBunsId.add(data.get_id());
            }
        }
        return listOfBunsId;
    }

    public List<String> getListOfSaucesId(IngredientsResponse allIngredients) {
        List<String> listOfSaucesId = new ArrayList<>();
        for (IngredientsData data : allIngredients.getData()) {
            if (data.getType().equals("sauce")) {
                listOfSaucesId.add(data.get_id());
            }
        }
        return listOfSaucesId;
    }

    public List<String> getListOfFillingsId(IngredientsResponse allIngredients) {
        List<String> listOfFillingsId = new ArrayList<>();
        for (IngredientsData data : allIngredients.getData()) {
            if (data.getType().equals("main")) {
                listOfFillingsId.add(data.get_id());
            }
        }
        return listOfFillingsId;
    }

    public OrdersRequest generateRandomBurger() {
        Random random = new Random();
        OrdersRequest ordersRequest = new OrdersRequest();
        IngredientsResponse allIngredients = getAllIngredients();
        int bunRandomIndex;
        int sauceRandomIndex;
        int fillingRandomIndex;
        List<String> randomBurger = new ArrayList<>();
        List<String> listOfBunsId = getListOfBunsId(allIngredients);
        List<String> listOfSaucesId = getListOfSaucesId(allIngredients);
        List<String> listOfFillingsId = getListOfFillingsId(allIngredients);

        bunRandomIndex = random.nextInt(listOfBunsId.size());
        sauceRandomIndex = random.nextInt(listOfSaucesId.size());
        fillingRandomIndex = random.nextInt(listOfFillingsId.size());
        randomBurger.add(listOfBunsId.get(bunRandomIndex));
        randomBurger.add(listOfFillingsId.get(fillingRandomIndex));
        randomBurger.add(listOfSaucesId.get(sauceRandomIndex));
        randomBurger.add(listOfBunsId.get(bunRandomIndex));
        ordersRequest.setIngredients(randomBurger);
        return ordersRequest;
    }
}
