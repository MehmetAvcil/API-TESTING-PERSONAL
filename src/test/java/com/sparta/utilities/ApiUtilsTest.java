package com.sparta.utilities;

import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ApiUtilsTest {

    @Test
    public void getBaseRequestSpec_setsCorrectBaseUriAndPath() {
        RequestSpecification spec = ApiUtils.getBaseRequestSpec();
        RequestSpecificationImpl impl = (RequestSpecificationImpl) spec;


        MatcherAssert.assertThat(impl.getBaseUri(), Matchers.is(ApiUtils.BASE_URI));
        MatcherAssert.assertThat(impl.getBasePath(), Matchers.is(ApiUtils.LOGIN_PATH));
        MatcherAssert.assertThat(impl.getContentType(), Matchers.is(ContentType.JSON.toString()));
    }

    @Test
    void getBearerRequestSpec_addsAuthHeaderWithBearerToken() {
        String token = "Abc123";
        RequestSpecification spec = ApiUtils.getBearerRequestSpec(token);
        RequestSpecificationImpl impl = (RequestSpecificationImpl) spec;

        MatcherAssert.assertThat(impl.getHeaders().getValue("Authorization"), Matchers.is("Bearer Abc123"));
        MatcherAssert.assertThat(impl.getBasePath(), Matchers.is("/api"));
    }
}
