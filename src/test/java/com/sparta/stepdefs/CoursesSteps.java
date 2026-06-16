package com.sparta.stepdefs;

import com.sparta.hooks.Hooks;
import com.sparta.models.CourseDTOPOJO;
import com.sparta.utilities.ApiUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import static com.sparta.utilities.ApiUtils.COURSES_PATH;
import static org.hamcrest.MatcherAssert.assertThat;

public class CoursesSteps {
    Response response;

    @Given("the courses endpoint is available")
    public void theCoursesEndpointIsAvailable() {
    }

    @When("the user sends a GET request to the courses endpoint")
    public void theUserSendsAGETRequestToTheCoursesEndpoint() {
        response = RestAssured
                .given(ApiUtils.getBearerRequestSpec(Hooks.token))
                .log().all()
                .when()
                .log().all()
                .get(COURSES_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertThat(response.statusCode(), Matchers.equalTo(expectedStatusCode));
    }

    @And("the response body should contain valid course data")
    public void theResponseBodyShouldContainValidCourseData() {
        CourseDTOPOJO[] courses = response.as(CourseDTOPOJO[].class);

        assertThat(courses, Matchers.not(""));
        assertThat(courses.length, Matchers.greaterThan(1));
        assertThat(courses[0].getId(), Matchers.isA(Integer.class));
        assertThat(courses[0].getStartDate(), Matchers.matchesPattern(("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));
        assertThat(courses[0].getEndDate(), Matchers.matchesPattern(("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")));
    }
}
