package ru.yandex.practicum.api.pojo;

import java.util.List;

public class OrdersRequest {
    List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
