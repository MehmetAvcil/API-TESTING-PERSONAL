package com.sparta.utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiUtils {

    public static final String BASE_URI = ConfigReader.get("base.uri");
    public static final String USERNAME = ConfigReader.get("username");
    public static final String PASSWORD = ConfigReader.get("password");

    public static final String LOGIN_PATH = ConfigReader.get("login.path");
    public static final String SPARTANS_PATH = ConfigReader.get("spartans.path");
    public static final String COURSES_PATH = ConfigReader.get("courses.path");

    public static RequestSpecification getBaseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(LOGIN_PATH)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getBearerRequestSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath("/api")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

}
