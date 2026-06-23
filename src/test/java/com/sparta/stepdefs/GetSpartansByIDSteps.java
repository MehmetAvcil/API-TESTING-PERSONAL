package com.sparta.stepdefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hooks.Hooks;
import com.sparta.models.SpartanDTOPOJO;
import com.sparta.utilities.ApiUtils;
import com.sparta.utilities.DataLoader;
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

public class GetSpartansByIDSteps {
    Response response;
    private Map<String, Object> testData;
    RequestSpecification requestSpec;
    String invalidToken;

    @Given("the admin is authenticated with valid credentials")
    public void theAdminIsAuthenticatedWithValidCredentials() {
        requestSpec = ApiUtils.getBearerRequestSpec(Hooks.token);
    }

    @Given("the admin is authenticated with invalid credentials")
    public void theAdminIsAuthenticatedWithInvalidCredentials() {
        requestSpec = ApiUtils.getBearerRequestSpec(invalidToken);
    }

    @When("the admin requests a Spartan profile with id {string} from {string}")
    public void theAdminRequestsASpartanProfileWithId(String idKey, String fileName) throws IOException {

        testData = DataLoader.getTestData(idKey, fileName);

        response = RestAssured
                .given(requestSpec)
                .pathParams(Map.of("id", testData.get("id")))
                .when()
                .get(ApiUtils.SPARTANS_PATH + "/{id}")
                .then()
                .log().all()
                .extract().response();
    }

    @Then("the status code must be {int}")
    public void theStatusCodeMustBe(int expectedStatus) {
        MatcherAssert.assertThat(response.statusCode(), Matchers.is(expectedStatus));
    }

    @And("the response body should contain the expected Spartan data")
    public void theResponseBodyShouldContainTheExpectedSpartanData() {
        SpartanDTOPOJO spartan = response.as(SpartanDTOPOJO.class);
        MatcherAssert.assertThat(spartan.getId(), Matchers.is(testData.get("id")));
        MatcherAssert.assertThat(spartan.getFirstName(), Matchers.not(""));
        MatcherAssert.assertThat(spartan.getLastName(), Matchers.not(""));
    }

    @And("the WWW-Authenticate header should contain the expected error message")
    public void theWWWAuthenticateHeaderShouldContainTheExpectedErrorMessage() {
        MatcherAssert.assertThat(
                response.getHeaders().getValue("WWW-Authenticate"),
                Matchers.is(testData.get("expectedErrorMessage"))
        );
    }

    @And("the response body should be empty")
    public void theResponseBodyShouldBeEmpty() {
        MatcherAssert.assertThat(response.body().asString(), Matchers.is(""));
    }

    @Then("the first name length should be at least {int} characters")
    public void theFirstNameLengthShouldBeAtLeastCharacters(int minLength) {
        SpartanDTOPOJO spartan = response.as(SpartanDTOPOJO.class);
        MatcherAssert.assertThat(spartan.getFirstName(), Matchers.hasLength(minLength));
    }

    @And("the last name length should be at least {int} characters")
    public void theLastNameLengthShouldBeAtLeastCharacters(int minLength) {
        SpartanDTOPOJO spartan = response.as(SpartanDTOPOJO.class);
        MatcherAssert.assertThat(spartan.getLastName(), Matchers.hasLength(minLength));
    }
}
