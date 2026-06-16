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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DeleteSpartansByIDSteps {
    Response createResponse;
    Response deleteResponse;
    RequestSpecification requestSpec;
    String errorMessage;
    private Map<String, Object> testData;
    Integer createdSpartanId;

    @Given("the administrators has an authorized session")
    public void theAdministratorsHasAnAuthorizedSession() {
        requestSpec = RestAssured.given(ApiUtils.getBearerRequestSpec(Hooks.token));
    }

    @And("an existing Spartan profile is available to delete using the {string} data from {string}")
    public void anExistingSpartanProfileIsAvailableToDelete(String profileKey, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/resources/testdata/" + fileName));
        SpartanPOJO spartan = mapper.treeToValue(root.get(profileKey), SpartanPOJO.class);
        testData = mapper.convertValue(root.get(profileKey), Map.class);
        createResponse = RestAssured
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

    @When("a DELETE request is sent to the Spartan profile endpoint")
    public void aDELETERequestIsSentToTheSpartanProfileEndpointUsingTheDataFrom() {
        deleteResponse = RestAssured
                .given(requestSpec)
                .pathParams(Map.of("id", testData.get("id")))
                .when()
                .delete(ApiUtils.SPARTANS_PATH + "/{id}")
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int responseCode) {
        MatcherAssert.assertThat(deleteResponse.statusCode(), Matchers.is(responseCode));
    }
}
