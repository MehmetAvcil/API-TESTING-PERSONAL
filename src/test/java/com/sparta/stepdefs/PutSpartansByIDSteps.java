package com.sparta.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PutSpartansByIDSteps {
    RequestSpecification requestSpec;
    Response response;
    String errorMessage;
    private Map<String, Object> testData;

    @Given("the admin has an authorized session")
    public void theAdminHasAnAuthorizedSession() {
        requestSpec = RestAssured.given(ApiUtils.getBearerRequestSpec(Hooks.token));
    }

    @When("a PUT request is sent to the Spartan profile endpoint using the {string} data from {string}")
    public void aPUTRequestIsSentToTheSpartanProfileEndpointUsingTheDataFrom(String profileKey, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/resources/testdata/" + fileName));
        SpartanPOJO spartan = mapper.treeToValue(root.get(profileKey), SpartanPOJO.class);
        testData = mapper.convertValue(root.get(profileKey), Map.class);
        response = RestAssured
                .given(requestSpec)
                .log().all()
                .body(spartan)
                .pathParams(Map.of("id", 1))
                .when()
                .log().all()
                .put(ApiUtils.SPARTANS_PATH+"/{id}")
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the API response code should be {int}")
    public void theAPIResponseCodeShouldBe(int responseCode) {
        MatcherAssert.assertThat(response.statusCode(), Matchers.equalTo(responseCode));
    }

    @And("the response body should reflect the updated profile details")
    public void theResponseBodyShouldReflectTheUpdatedProfileDetails() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("the response body should contain a validation error message")
    public void theResponseBodyShouldContainAValidationErrorMessage() {
        errorMessage = testData.get("expectedError").toString();
        MatcherAssert.assertThat(response.body().asString(), Matchers.containsString(errorMessage));
    }
}
