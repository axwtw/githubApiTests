package com.github.test;

import com.github.framework.RestAssuredConfiguration;
import com.github.test.common.EndPoints;
import com.github.test.model.User;
import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class Tests {

    private String clientId = "93c2f54e1f30e6c6607d";
    private String clientSecret = "ef7c61aacf8fcd745b4b0332fee954d161e3e904";

    @Test(groups = "demo")
    public void verifyUserFollowersAndFollowing() {
        RequestSpecification requestSpecification = new RestAssuredConfiguration().getRequestSpecification();
        requestSpecification.accept(ContentType.JSON).pathParam("user", "maywthr").log().all();
        given()
                .spec(requestSpecification)
                .get(EndPoints.GET_USER)
                .then().statusCode(200)
                .log()
                .all();

        Response response = given()
                .spec(requestSpecification)
                .get(EndPoints.GET_USER);

        User user = response.as(User.class);
        Assert.assertEquals(user.getFollowers(), 0);
        Assert.assertEquals(user.getFollowing(), 2);
    }

    @Test(groups = "demo")
    public void automatedOAuthAuthorizationForNonWebApp() {

        given()
                .auth()
                .preemptive()
                .basic("maywthr", "Ukraine78")
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
