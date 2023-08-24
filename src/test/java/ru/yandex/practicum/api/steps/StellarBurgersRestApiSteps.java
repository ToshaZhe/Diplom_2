package ru.yandex.practicum.api.steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.yandex.practicum.api.configs.Config;

public class StellarBurgersRestApiSteps {
    public RequestSpecification getDefaultRequestSpec() {
        return new RequestSpecBuilder().setBaseUri(Config.getBaseUrl()).setContentType(ContentType.JSON).build();
    }
}
