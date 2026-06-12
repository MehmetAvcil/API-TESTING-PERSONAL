package com.sparta.stepdefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hooks.Hooks;
import com.sparta.models.SpartanPOJO;
import com.sparta.utilities.ApiUtils;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GetSpartansSteps {

    RequestSpecification requestSpecification;
    Response response;
    private Map<String, Object> testData;

    @Given("the user has an authorized session")
    public void theUserHasAnAuthorizedSession() {
        requestSpecification = RestAssured
                .given(ApiUtils.getBearerRequestSpec(Hooks.token));
    }

    @When("the user sends a GET request to the Spartans endpoint")
    public void theUserSendsAGETRequestToTheSpartansEndpoint() {
        response = requestSpecification
                .when()
                .log().all()
                .get(ApiUtils.SPARTANS_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the Spartan registry response status code should be {int}")
    public void theSpartanRegistryResponseStatusCodeShouldBe(int expectedStatusCode) {
        MatcherAssert.assertThat(response.getStatusCode(), Matchers.is(expectedStatusCode));
    }

    @And("the response body should contain valid Spartans data")
    public void theResponseBodyShouldContainValidSpartansData() {
        SpartanPOJO[] spartans = response.as(SpartanPOJO[].class);

        MatcherAssert.assertThat(spartans, Matchers.not(""));
        MatcherAssert.assertThat(spartans.length, Matchers.greaterThan(1));
        MatcherAssert.assertThat(spartans[0].getId(), Matchers.not(""));
        MatcherAssert.assertThat(spartans[0].getFirstName(), Matchers.not(""));
        MatcherAssert.assertThat(spartans[0].getLastName(), Matchers.not(""));
        MatcherAssert.assertThat(spartans[0].isGraduated(), Matchers.not(""));
    }

    @Given("the user has an unauthorized session with token {string} from {string}")
    public void theUserHasAnUnauthorizedSessionWithTokenFrom(String testCase, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/resources/testdata/" + fileName));
        testData = mapper.convertValue(root.get(testCase), Map.class);

        requestSpecification =  RestAssured
                .given(ApiUtils.getBearerRequestSpec(testData.get("token").toString()))
                .log().all();
    }

    @And("the response header error message be expected file message")
    public void theResponseHeaderErrorMessageBeExpectedFileMessage() {
        MatcherAssert.assertThat(response.getHeaders().getValue("WWW-Authenticate"), Matchers.is(testData.get("expectedErrorMessage")));
    }
}
