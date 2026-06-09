package utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiUtils {

    public static final String BASE_URI = "http://localhost:8080/";
    public static final String USERNAME = "sparta";
    public static final String PASSWORD = "global";

    public static final String LOGIN_PATH = "/Auth/login";
    public static final String SPARTANS_PATH = "/Spartans";
    public static final String COURSES_PATH = "/Courses";

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
