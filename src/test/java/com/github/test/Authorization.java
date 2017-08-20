package com.github.test;

import com.github.test.common.EndPoints;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;


public class Authorization {

    private String clientId = "93c2f54e1f30e6c6607d";
    private String clientSecret = "ef7c61aacf8fcd745b4b0332fee954d161e3e904";

    @Test(groups = "demo")
    public void automatedOAuthAuthorizationForNonWebApp() {

        given()
                .auth()
                .preemptive()
                .basic("maywthr", System.getenv("git.pass"))
                .param("note", "admin script")
                .param("scopes", "[ ]")
                .param("client_id", clientId)
                .param("client_secret", clientSecret)
        .when()
                .get(EndPoints.GITHUB_AUTHORIZATIONS_URL)
        .then()
                .statusCode(200)
                .log()
                .all();
    }

}
