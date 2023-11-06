package tests;

import models.ListModels.ListUsersDataResponseModel;
import models.ListModels.ListUsersResponseModel;
import models.ListModels.ListUsersSupportDataResponseModel;
import models.LoginBodyModel;
import models.LoginResponseModel;
import models.MissingPasswordResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.DeleteUserSpec.deleteUserRequestSpec;
import static specs.DeleteUserSpec.deleteUserResponseSpec;
import static specs.ListUsersSpec.listUsersRequestSpec;
import static specs.ListUsersSpec.listUsersResponseSpec;
import static specs.LoginSpec.*;
import static specs.NotFoundUserSpec.notFoundUserRequestSpec;
import static specs.NotFoundUserSpec.notFoundUserResponseSpec;

public class ReqresTests extends TestBase{

    @DisplayName("Получение списка пользователей")
    @Test
    void getListUsersTest() {
        ListUsersResponseModel responseModel = step ("Запрос на получение списка пользователей", () ->
        given(listUsersRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(listUsersResponseSpec)
                .extract().as(ListUsersResponseModel.class));

        step ("Проверка общих данных", () -> {
                assertEquals(2, responseModel.getPage());
                assertEquals(6, responseModel.getPerPage());
                assertEquals(12, responseModel.getTotal());
                assertEquals(2, responseModel.getTotalPages());
    });
        step("Проверка данных первого пользователя из списка", () -> {
            List<ListUsersDataResponseModel> data = responseModel.getData();
            assertEquals(7, data.get(0).getId());
            assertEquals("michael.lawson@reqres.in", data.get(0).getEmail());
            assertEquals("Michael", data.get(0).getFirstName());
            assertEquals("Lawson", data.get(0).getLastName());
            assertEquals("https://reqres.in/img/faces/7-image.jpg", data.get(0).getAvatar());
        });

        step("Проверка данных о поддержке", () -> {
            ListUsersSupportDataResponseModel support = responseModel.getSupport();
            assertEquals("https://reqres.in/#support-heading", support.getUrl());
            assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", support.getText());
        });
    }

    @DisplayName("Поиск отсутствующего пользователя")
    @Test
    void getSingleUserNotFoundTest() {
        step("Запрос на поиск пользователя", () -> {
            given(notFoundUserRequestSpec)
                    .when()
                    .get("/users/50")
                    .then()
                    .spec(notFoundUserResponseSpec);
        });
    }

    @DisplayName("Удаление пользователя")
    @Test
    void deleteUserTest() {
        step("Запрос на удаление пользователя", () -> {
            given(deleteUserRequestSpec)
                    .when()
                    .delete("/users/5")
                    .then()
                    .spec(deleteUserResponseSpec);
        });
    }

    @DisplayName("Удачная авторизация пользователя")
    @Test
    void postLoginSuccessfullTest() {
        LoginBodyModel autData = new LoginBodyModel();
        autData.setEmail("eve.holt@reqres.in");
        autData.setPassword("cityslicka");

            LoginResponseModel responseModel = step ("Запрос логина и пароля", () ->
                given(loginRequestSpec)
                    .body(autData)
                    .when()
                    .post("/login")
                    .then()
                     .spec(loginResponseSpec)
                    .extract().as(LoginResponseModel.class));

        step ("Проверка токена", () ->
            assertEquals("QpwL5tke4Pnpja7X4", responseModel.getToken()));

    }

    @DisplayName("Неудачная авторизация пользователя (отсутствует пароль)")
    @Test
    void postLoginUnsuccessfullTest() {
        LoginBodyModel autData = new LoginBodyModel();
        autData.setEmail("eve.holt@reqres.in");

        MissingPasswordResponseModel responseModel = step ("Запрос логина", () ->
                given(loginRequestSpec)
                    .body(autData)
                    .when()
                    .post("/login")
                    .then()
                    .spec(missingPasswordResponseSpec)
                    .extract().as(MissingPasswordResponseModel.class));

        step("Проверка текста ошибки", () ->
                assertEquals("Missing password", responseModel.getError()));
    }
}
