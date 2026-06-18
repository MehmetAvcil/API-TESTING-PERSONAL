package com.sparta.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.sparta.utilities.DataLoader.mapper;

public class PostSpartansSteps {
    RequestSpecification requestSpec;
    Response response;
    Response deleteResponse;
    String errorMessage;
    Response getResponse;
    private Map<String, Object> testData;


    @Given("the administrator has an authorized session")
    public void theAdministratorHasAnAuthorizedSession() {
        requestSpec = RestAssured.given(ApiUtils.getBearerRequestSpec(Hooks.token));
    }

    @When("a POST request is sent to the Spartan profile endpoint using the {string} data from {string}")
    public void aPOSTRequestIsSentToTheSpartanProfileEndpointUsingTheDataFrom(String profileKey, String fileName) throws IOException {
        testData = DataLoader.getTestData(profileKey, fileName);
        SpartanPOJO spartan = new ObjectMapper().convertValue(testData, SpartanPOJO.class);

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


        deleteResponse = RestAssured
                .given(ApiUtils.getBearerRequestSpec(Hooks.token))
                .pathParams(Map.of("id", testData.get("id")))
                .when()
                .delete(ApiUtils.SPARTANS_PATH + "/{id}")
                .then()
                .log().all()
                .extract().response();

    }

    @Then("the API response status code should be {int}")
    public void theAPIResponseStatusCodeShouldBe(int responseCode) {
        MatcherAssert.assertThat(response.statusCode(), Matchers.equalTo(responseCode));
    }

    @And("the API body should contain the newly created Spartan's ID")
    public void theResponseBodyShouldContainTheNewlyCreatedSpartanSID() {
        getResponse = RestAssured
                .given(ApiUtils.getBearerRequestSpec(Hooks.token))
                .pathParams(Map.of("id", testData.get("id")))
                .when()
                .get(ApiUtils.SPARTANS_PATH + "/{id}")
                .then()
                .log().all()
                .extract().response();

        MatcherAssert.assertThat(getResponse.statusCode(), Matchers.equalTo(200));
    }


    @And("the response body should contain the expected validation error message")
    public void theResponseBodyShouldContainAValidationErrorMessage() {
        errorMessage = testData.get("expectedError").toString();
        MatcherAssert.assertThat(response.body().asString(), Matchers.containsString(errorMessage));
    }

}
