package com.example.reservisland;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ReservislandApplication.class })
@Execution(ExecutionMode.SAME_THREAD)
public class ControllerIntegrationTests {

    @Autowired
    protected ObjectMapper objectMapper;

    public RequestSpecBuilder createRequestBuilder() {
        return new RequestSpecBuilder().addHeaders(new HashMap<>());
    }

    public RequestSpecBuilder createRequestBuilder(final Object body) {
        return createRequestBuilder()
                .setContentType(ContentType.JSON)
                .setBody(body);
    }

    public ResponseSpecBuilder createResponseBuilder(final HttpStatus statusCode) {
        return new ResponseSpecBuilder().expectStatusCode(statusCode.value());
    }

    public String get(String url, RequestSpecification specRequest, ResponseSpecification specResponse) {
        return given()
                .spec(specRequest)
                .expect()
                .log().all()
                .spec(specResponse)
                .when()
                .get(url)
                .asString();
    }

    public String post(String url, RequestSpecification specRequest, ResponseSpecification specResponse) {
        return given()
                .log().all()
                .spec(specRequest)
                .expect()
                .log().all()
                .spec(specResponse)
                .when()
                .post(url)
                .asString();
    }

    public String put(String url, RequestSpecification specRequest, ResponseSpecification specResponse) {
        return given()
                .log().all()
                .spec(specRequest)
                .expect()
                .log().all()
                .spec(specResponse)
                .when()
                .put(url)
                .asString();
    }

    public String delete(String url, RequestSpecification specRequest, ResponseSpecification specResponse) {
        return given()
                .log().all()
                .spec(specRequest)
                .expect()
                .log().all()
                .spec(specResponse)
                .when()
                .delete(url)
                .asString();
    }
}
