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

public class PostSpartansSteps {
    RequestSpecification requestSpec;
    Response response;
    String errorMessage;
    private Map<String, Object> testData;


    @Given("the administrator has an authorized session")
    public void theAdministratorHasAnAuthorizedSession() {
        requestSpec = RestAssured.given(ApiUtils.getBearerRequestSpec(Hooks.token));
    }

    @When("a POST request is sent to the Spartan profile endpoint using the {string} data from {string}")
    public void aPOSTRequestIsSentToTheSpartanProfileEndpointUsingTheDataFrom(String profileKey, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/resources/testdata/" + fileName));
        SpartanPOJO spartan = mapper.treeToValue(root.get(profileKey), SpartanPOJO.class);
        testData = mapper.convertValue(root.get(profileKey), Map.class);
        response = RestAssured
                .given(requestSpec)
                .log().all()
                .body(spartan)
                .when()
                .log().all()
                .post(ApiUtils.SPARTANS_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the API response status code should be {int}")
    public void theAPIResponseStatusCodeShouldBe(int responseCode) {
        MatcherAssert.assertThat(response.statusCode(), Matchers.equalTo(responseCode));
    }

    @And("the response body should contain the newly created Spartan's ID")
    public void theResponseBodyShouldContainTheNewlyCreatedSpartanSID() {
    }

    @And("the response body should reflect the submitted profile details")
    public void theResponseBodyShouldReflectTheSubmittedProfileDetails() {
    }


    @And("the response body should contain the expected validation error message")
    public void theResponseBodyShouldContainAValidationErrorMessage() {
        errorMessage = testData.get("expectedError").toString();
        MatcherAssert.assertThat(response.body().asString(), Matchers.containsString(errorMessage));
    }

}
