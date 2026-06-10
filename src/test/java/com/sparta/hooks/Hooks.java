package com.sparta.hooks;

import com.sparta.utilities.ApiUtils;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;

import java.util.Map;

import static com.sparta.utilities.ApiUtils.*;
import static io.restassured.RestAssured.given;

public class Hooks {
    public static String token;

    @BeforeAll
    public static void setup() {
        token =  given(ApiUtils.getBaseRequestSpec())
                .body(Map.of("username", USERNAME, "password", PASSWORD))
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().path("token");
    }
}
