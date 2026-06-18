package com.sparta.stepdefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.utilities.DataLoader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import com.sparta.utilities.ApiUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LoginSteps {

    private Response response;
    private Map<String, Object> testData;

    @Given("the user credentials are {string} from {string}")
    public void theUserCredentialsAre(String testCaseKey, String fileName) throws IOException {
        testData = DataLoader.getTestData(testCaseKey, fileName);
    }

    @When("the user sends a POST request to the login endpoint")
    public void theUserSendsAPOSTRequestToTheLoginEndpoint() {
        response = RestAssured
                .given(ApiUtils.getBaseRequestSpec())
                .body(Map.of(
                        "username", testData.get("username"),
                        "password", testData.get("password")
                ))
                .log().all()
                .when()
                .log().all()
                .post()
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the response status code must match the expected value")
    public void theResponseStatusCodeMustMatchTheExpectedValue() {
        MatcherAssert.assertThat(response.statusCode(), Matchers.is(testData.get("expectedStatusCode")));
    }
}
