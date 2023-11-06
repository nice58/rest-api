package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.RestAssured.with;

public class DeleteUserSpec {

        public static RequestSpecification deleteUserRequestSpec = with()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .contentType(JSON);

        public static ResponseSpecification deleteUserResponseSpec = new ResponseSpecBuilder()
                .log(STATUS)
                .log(BODY)
                .expectStatusCode(204)
                .build();
}
