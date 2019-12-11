package com.elamotte.quarkus.poc.it.rest;

import com.elamotte.quarkus.poc.util.TokenUtils;
import com.nimbusds.jose.JOSEException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;

@QuarkusTest
@Transactional
public class UserResourceTest {
    private String token;

    @Test
    void get() throws JOSEException {
        token = TokenUtils.CreateToken("user1");

        given().auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .when()
                .get("/secure/user")
                .then()
                .statusCode(200);

        given().contentType(ContentType.JSON)
                .when()
                .get("/secure/user")
                .then()
                .statusCode(401);

        token = TokenUtils.CreateToken("user2");

        given().auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .when()
                .get("/secure/user")
                .then()
                .statusCode(403);
    }
}
