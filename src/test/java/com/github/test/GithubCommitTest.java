package com.github.test;

import com.github.framework.RestAssuredConfiguration;
import com.github.test.common.EndPoints;
import com.github.test.common.PathParams;
import com.github.test.model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by sergey on 8/17/17.
 */
public class GithubCommitTest {

    private final String AUTH_TOKEN = System.getenv("github.token");

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
    public void singleCommitAuthorValidation() {
        given()
                .pathParam("user", PathParams.USER)
                .pathParam("repo", PathParams.REPO)
                .pathParam("sha", PathParams.SHA)
        .when()
                .get(EndPoints.GET_USER_COMMIT_BY_SHA)
        .then()
                .statusCode(200)
                .body("commit.author.name", equalTo("Sergey Degtyaryov"))
        .and()
                .body("commit.author.email", equalTo("axwtww@gmail.com"));
    }

    @Test(groups = "demo")
    public void singleCommitCommitterValidation() {
        given()
                .pathParam("user", PathParams.USER)
                .pathParam("repo", PathParams.REPO)
                .pathParam("sha", PathParams.SHA)
        .when()
                .get(EndPoints.GET_USER_COMMIT_BY_SHA)
        .then()
                .statusCode(200)
                .body("commit.committer.name", equalTo("Sergey Degtyaryov"))
        .and()
                .body("commit.committer.email", equalTo("axwtww@gmail.com"));
    }


    //818af8a09936fa27cd89f1bf393e27e3a30d92b7 mwthr token
    @Test(groups = "demo")
    public void addNewCommentToSingleCommit() {

        //json dataprovider
        Map<String, Object> jsonAsMap = new HashMap<String, Object>();
        jsonAsMap.put("body", "test comment " + System.currentTimeMillis());
        jsonAsMap.put("path", "");
        jsonAsMap.put("position", "4");
        jsonAsMap.put("line", "null");

        //check comments count before new comment added
        int commentCount =
                given()
                        .pathParam("user", PathParams.USER)
                        .pathParam("repo", PathParams.REPO)
                        .pathParam("sha", PathParams.SHA)
                .when()
                        .get(EndPoints.GET_USER_COMMIT_BY_SHA)
                .then()
                        .statusCode(200)
                .extract()
                        .path("commit.comment_count");

        //add new comment for single commit
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", AUTH_TOKEN)
                .body(jsonAsMap)
        .when()
                .post(EndPoints.GET_USER_COMMIT_COMMENTS_URL)
        .then()
                .statusCode(201)
                .log()
                .all();

        //verify that count of comments increased by 1
        given()
                .pathParam("user", PathParams.USER)
                .pathParam("repo", PathParams.REPO)
                .pathParam("sha", PathParams.SHA)
        .when()
                .get(EndPoints.GET_USER_COMMIT_BY_SHA)
        .then()
                .statusCode(200)
                .body("commit.comment_count", equalTo(++commentCount));
    }

    @Test(groups = "demo")
    public void addCommitCommentAndRemove() {
        //json dataprovider
        Map<String, Object> jsonAsMap = new HashMap<String, Object>();
        jsonAsMap.put("body", "test comment " + System.currentTimeMillis());
        jsonAsMap.put("path", "");
        jsonAsMap.put("position", "4");
        jsonAsMap.put("line", "null");

        //add new comment for single commit and extract id of comment
        Integer id =
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header("Authorization", AUTH_TOKEN)
                .body(jsonAsMap)
        .when()
                .post(EndPoints.GET_USER_COMMIT_COMMENTS_URL)
        .then()
                .statusCode(201)
                .log()
                .all()
        .and()
                .extract()
                .path("id");

        //remove comment by extracted id before
        given()
                .header("Authorization", AUTH_TOKEN)
                .pathParam("user", PathParams.USER)
                .pathParam("repo", PathParams.REPO)
                .pathParam("id", id)
        .when()
                .delete(EndPoints.GITHUB_COMMENT_ID_URL)
        .then()
                .statusCode(204)
                .log()
                .all();
    }

    @Test(groups = "demo")
    public void addCommitCommentAndUpdate() {

        //json dataprovider
        Map<String, Object> jsonAsMap = new HashMap<String, Object>();
        jsonAsMap.put("body", "test comment " + System.currentTimeMillis());
        jsonAsMap.put("path", "");
        jsonAsMap.put("position", "4");
        jsonAsMap.put("line", "null");

        //add new comment for single commit and extract id of comment
        Integer id =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .header("Authorization", AUTH_TOKEN)
                        .body(jsonAsMap)
                .when()
                        .post(EndPoints.GET_USER_COMMIT_COMMENTS_URL)
                .then()
                        .statusCode(201)
                        .log()
                        .all()
                .and()
                        .extract()
                        .path("id");

        //remove comment by extracted id before
        given()
                .header("Authorization", AUTH_TOKEN)
                .pathParam("user", "axwtw")
                .pathParam("repo", "fitbit-tests")
                .pathParam("id", id)
                .body(String.format("{\"body\": \"%s\"}", "Updated comment"))
                .when()
                .patch(EndPoints.GITHUB_COMMENT_ID_URL)
                .then()
                .statusCode(200)
                .log()
                .all();
    }

}
