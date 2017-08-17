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

    @Test(groups = "demo")
    public void firstTest3() {
        RequestSpecification requestSpecification = new RestAssuredConfiguration().getRequestSpecification();
        requestSpecification.accept(ContentType.JSON).pathParam("user", "GrahamCampbell").log().all();
        given().spec(requestSpecification).get(EndPoints.GET_USER).then().statusCode(200).log().all();

        Response response = given().spec(requestSpecification).get(EndPoints.GET_USER);

        User user = response.as(User.class);
        Assert.assertEquals(user.getFollowers(), 3216);
        Assert.assertEquals(user.getFollowing(), 69);



    }


}
