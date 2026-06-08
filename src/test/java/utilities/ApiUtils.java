package utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiUtils {

    public static final String LOGIN_PATH = "/Auth/login";
    public static final String SPARTANS_PATH = "/api/spartans";
    public static final String COURSES_PATH = "/api/Courses";

    public static RequestSpecification getBaseRequestSpec() {
        return RestAssured.given()
                .baseUri(ConfigurationReader.getProperty("base_url"))
                .contentType(ContentType.JSON)
                .log().all();
    }

}
