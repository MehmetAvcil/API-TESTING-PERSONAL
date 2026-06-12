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
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.either;

public class GetSpartansByIDSteps {
    Response response;
    private Map<String, Object> testData;
    boolean credentials;
    String invalidToken;

    @Given("the admin has credentials {string} from {string}")
    public void theAdminHasCredentialsFrom(String scenario, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("src/test/resources/testdata/" + fileName));
        testData = mapper.convertValue(root.get(scenario), Map.class);
        credentials = (boolean) testData.get(("credentials"));
    }

    @When("the framework requests a single profile using an id")
    public void theFrameworkRequestsASingleProfileUsingAnId() {
        if (credentials) {
            response = RestAssured
                    .given(ApiUtils.getBearerRequestSpec(Hooks.token))
                    .pathParams(Map.of("id", testData.get("id")))
                    .when()
                    .get(ApiUtils.SPARTANS_PATH + "/{id}" )
                    .then()
                    .log().all()
                    .extract().response();
        } else {
            response = RestAssured
                    .given(ApiUtils.getBearerRequestSpec(invalidToken))
                    .when()
                    .get(ApiUtils.SPARTANS_PATH)
                    .then()
                    .log().all()
                    .extract().response();
        }

    }

    @Then("the profile response status code must match the file expectation")
    public void theProfileResponseStatusCodeMustMatchTheFileExpectation() {
        MatcherAssert.assertThat(response.statusCode(), Matchers.is(testData.get("expectedStatusCode")));
    }

    @And("the response body should contain valid Spartan data if applicable")
    public void theResponseBodyShouldContainValidSpartanData() {
        if (credentials && response.statusCode() == 200) {
            SpartanPOJO spartans = response.as(SpartanPOJO.class);

            MatcherAssert.assertThat(spartans.getId(), Matchers.is(testData.get("id")));
            MatcherAssert.assertThat(spartans.getFirstName(), Matchers.not(""));
            MatcherAssert.assertThat(spartans.getLastName(), Matchers.not(""));

        } else if (!credentials) {
            MatcherAssert.assertThat(response.getHeaders().getValue("WWW-Authenticate"), (Matchers.is(testData.get("expectedErrorMessage"))));
        }

        else {
            MatcherAssert.assertThat(response.body().asString(), Matchers.is(""));

        }
    }
}
