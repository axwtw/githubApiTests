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

//    private final String API_BASE = "https://api.github.com";
    private final String OAUTH_GITHUB_URI = "http://github.com/login/oauth/authorize";
    private final String GITHUB_URI = "https://api.github.com/";
    private final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private String clientId = "93c2f54e1f30e6c6607d";
    private String clientSecret = "ef7c61aacf8fcd745b4b0332fee954d161e3e904";
    private String redirectUri = "http://localhost:8080/oauthcallback";

//    @Test(groups = "demo")
    public void firstTest3() {
        RequestSpecification requestSpecification = new RestAssuredConfiguration().getRequestSpecification();
        requestSpecification.accept(ContentType.JSON).pathParam("user", "GrahamCampbell").log().all();
        given().spec(requestSpecification).get(EndPoints.GET_USER).then().statusCode(200).log().all();

        Response response = given().spec(requestSpecification).get(EndPoints.GET_USER);

        User user = response.as(User.class);
        Assert.assertEquals(user.getFollowers(), 3216);
        Assert.assertEquals(user.getFollowing(), 69);
    }

    @Test
    public void automatedOAuthAuthorizationForNonWebApp() {

        given()
                .auth()
                .preemptive()
                .basic("maywthr", "Ukraine78")
                .param("note", "admin script")
                .param("scopes", "[ ]")
                .param("client_id","93c2f54e1f30e6c6607d")
                .param("client_secret", "ef7c61aacf8fcd745b4b0332fee954d161e3e904")
        .when()
                .get("https://api.github.com/authorizations")
        .then()
                .statusCode(200)
                .log()
                .all();
    }


}
