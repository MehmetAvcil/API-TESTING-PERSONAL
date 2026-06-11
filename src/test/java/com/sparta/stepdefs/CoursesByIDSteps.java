package com.sparta.stepdefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hooks.Hooks;
import com.sparta.models.CoursePOJO;
import com.sparta.utilities.ApiUtils;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.types.Hook;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
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
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/resources/testdata/" + fileName));
        testData = mapper.convertValue(root.get(testCaseKey), Map.class);

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
        CoursePOJO course = response.as(CoursePOJO.class);

        MatcherAssert.assertThat(course.getName(), Matchers.notNullValue());
        MatcherAssert.assertThat(course.getTrainer(), Matchers.notNullValue());
        MatcherAssert.assertThat(course.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(course.getSpartans(), Matchers.notNullValue());
    }

    @And("the error message should indicate the value is invalid")
    public void theResponseBodyShouldContainErrorMessageThatIncludes() {
        MatcherAssert.assertThat(response.body().asString(), either(Matchers.containsString("is not valid"))
                .or(Matchers.containsString("is invalid")));
    }
}
