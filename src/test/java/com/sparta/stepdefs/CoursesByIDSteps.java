package com.sparta.stepdefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hooks.Hooks;
import com.sparta.models.CourseDTOPOJO;
import com.sparta.utilities.ApiUtils;
import com.sparta.utilities.DataLoader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.sparta.utilities.ApiUtils.COURSES_PATH;
import static org.hamcrest.CoreMatchers.either;

public class CoursesByIDSteps {
    Response response;
    Map<String, Object> testData;

    @Given("the courses by ID endpoint is available")
    public void theCoursesByIDEndpointIsAvailable() {
    }

    @When("the user sends a GET request for a single course using id {string} from {string}")
    public void theUserSendsAGETRequestForASingleCourseUsingIdFrom(String testCaseKey, String fileName) throws IOException {
        testData = DataLoader.getTestData(testCaseKey, fileName);

        response = RestAssured
                .given(ApiUtils.getBearerRequestSpec(Hooks.token))
                .log().all()
                .pathParams(Map.of(
                        "id", testData.get("id")
                ))
                .when()
                .log().all()
                .get(COURSES_PATH + "/{id}")
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the status code must match the file expectation")
    public void theStatusCodeMustMatchTheFileExpectation() {
        MatcherAssert.assertThat(response.statusCode(), Matchers.is(testData.get("expectedStatusCode")));
    }

    @And("the response body should contain expected course data")
    public void theResponseBodyShouldContainExpectedCourseData() {
        CourseDTOPOJO course = response.as(CourseDTOPOJO.class);
        MatcherAssert.assertThat(course.getId(), Matchers.is(testData.get("id")));
        MatcherAssert.assertThat(course.getSpartans(), Matchers.not(""));
    }

    @And("the error message should indicate the value is invalid")
    public void theResponseBodyShouldContainErrorMessageThatIncludes() {
        MatcherAssert.assertThat(response.body().asString(), either(Matchers.containsString("is not valid"))
                .or(Matchers.containsString("is invalid")));
    }
}
