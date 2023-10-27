import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresTests extends TestBase{

    @DisplayName("Получение списка пользователей")
    @Test

    void getListUsersTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12))
                .body("page", is(2));
    }

    @DisplayName("Поиск отсутствующего пользователя")
    @Test

    void getSingleUserNotFoundTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users/50")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @DisplayName("Удаление пользователя")
    @Test

    void deleteUserTest() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .delete("/users/5")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }

    @DisplayName("Удачная авторизация пользователя")
    @Test

    void postLoginSuccessfull() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}")
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @DisplayName("Неудачная авторизация пользователя (отсутствует пароль)")
    @Test

    void postLoginUnsuccessfull() {
        given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body("{\"email\": \"peter@klaven\"}")
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
