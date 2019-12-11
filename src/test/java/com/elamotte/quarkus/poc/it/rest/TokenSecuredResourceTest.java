package com.elamotte.quarkus.poc.it.rest;

import com.elamotte.quarkus.poc.util.TokenUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Tests of the TokenSecuredResource REST endpoints
 */
@Disabled
@QuarkusTest
public class TokenSecuredResourceTest {

    /**
     * The test generated JWT token string
     */
    private String token;

    @BeforeEach
    public void generateToken() throws Exception {
        HashMap<String, Long> timeClaims = new HashMap<>();
        token = TokenUtils.generateTokenString("/GoogleJwtClaims.json", timeClaims);
    }

    @Test
    public void testHelloEndpoint() {
        Response response = given()
          .when()
          .get("/secured/permit-all")
          .andReturn();

        System.out.printf("+++ testHelloEndpoint, response: %s\n", response.asString());
        response.then()
          .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
          //.body(containsString("hello + anonymous, isSecure: false"));
                .body(containsString("#### invalid token :"));
    }

    @Test
    public void testHelloRolesAllowed() {
        Response response = given().auth()
                .oauth2(token)
                .when()
                .get("/secured/roles-allowed").andReturn();

        Assertions.assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, response.getStatusCode());
        String replyString = response.body().asString();
        System.out.println(replyString);
    }

    @Test
    public void testHelloDenyAll() {
        Response response = given().auth()
                .oauth2(token)
                .when()
                .get("/secured/deny-all").andReturn();

        Assertions.assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, response.getStatusCode());
        String replyString = response.body().asString();
        System.out.println(replyString);
    }

}
