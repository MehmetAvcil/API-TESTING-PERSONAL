package com.sparta.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hooks.Hooks;
import com.sparta.models.SpartanPOJO;
import com.sparta.utilities.ApiUtils;
import com.sparta.utilities.DataLoader;
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

import java.io.IOException;
import java.util.Map;

public class PutSpartansByIDSteps {
    RequestSpecification requestSpec;
    Response response;
    Response createResponse;
    Response getResponse;
    String errorMessage;
    private Map<String, Object> putTestData;
    private Map<String, Object> createTestData;

    @Given("the admin has an authorized session")
    public void theAdminHasAnAuthorizedSession() {
        requestSpec = RestAssured.given(ApiUtils.getBearerRequestSpec(Hooks.token));
    }

    @When("a PUT request is sent to the Spartan profile endpoint using the {string} data from {string}")
    public void aPUTRequestIsSentToTheSpartanProfileEndpointUsingTheDataFrom(String profileKey, String fileName) throws IOException {
        putTestData = DataLoader.getTestData(profileKey, fileName);
        SpartanPOJO putSpartan = new ObjectMapper().convertValue(putTestData, SpartanPOJO.class);

        createTestData = DataLoader.getTestData("create_spartan", fileName);
        SpartanPOJO createSpartan = new ObjectMapper().convertValue(createTestData, SpartanPOJO.class);

        createResponse = RestAssured
                .given(requestSpec)
                .body(createSpartan)
                .pathParams(Map.of("id", createSpartan.getId()))
                .when()
                .put(ApiUtils.SPARTANS_PATH+"/{id}")
                .then()
                .log().all()
                .extract().response();



        response = RestAssured
                .given(requestSpec)
                .log().all()
                .body(putSpartan)
                .pathParams(Map.of("id", putSpartan.getId()))
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

    @And("the API should reflect the updated profile details")
    public void theResponseBodyShouldReflectTheUpdatedProfileDetails() {
        getResponse = RestAssured
                .given(ApiUtils.getBearerRequestSpec(Hooks.token))
                .pathParams(Map.of("id", putTestData.get("id")))
                .when()
                .get(ApiUtils.SPARTANS_PATH + "/{id}")
                .then()
                .log().all()
                .extract().response();

        MatcherAssert.assertThat(getResponse.body().asString(), Matchers.containsString("Avcilb"));
    }

    @And("the response body should contain a validation error message")
    public void theResponseBodyShouldContainAValidationErrorMessage() {
        errorMessage = putTestData.get("expectedError").toString();
        MatcherAssert.assertThat(response.body().asString(), Matchers.containsString(errorMessage));
    }
}
