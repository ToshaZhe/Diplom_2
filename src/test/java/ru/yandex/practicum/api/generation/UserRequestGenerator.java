package ru.yandex.practicum.api.generation;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practicum.api.pojo.UserRequest;

public class UserRequestGenerator {
    public static UserRequest getRandomUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(6) + "@ya.ru");
        userRequest.setPassword("qwerty");
        userRequest.setName("testName");
        return userRequest;
    }

    public static UserRequest getUserRequestWithoutEmail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("qwerty");
        userRequest.setName("testName");
        return userRequest;
    }

    public static UserRequest getUserRequestWithoutPassword() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(6) + "@ya.ru");
        userRequest.setName("testName");
        return userRequest;
    }

    public static UserRequest getUserRequestWithoutName() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(6) + "@ya.ru");
        userRequest.setPassword("qwerty");
        return userRequest;
    }
}
