package com.github.test;

import com.github.framework.RestAssuredConfiguration;
import com.github.test.bin.Commit;
import com.github.test.bin2.SingleCommit;
import com.github.test.common.EndPoints;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by sergey on 8/17/17.
 */
public class GithubCommitTest {

    private final String AUTH_TOKEN = "token 7d0381544746abe5d4a1182b88fc5b160315c32b";

    @Test(groups = "11")
    public void commitTest() {
        RequestSpecification requestSpecification = new RestAssuredConfiguration().getRequestSpecification();
        requestSpecification.accept(ContentType.JSON).pathParam("user", "axwtw").pathParam("repo", "fitbit-tests").log().all();
        Response response = new RestAssuredConfiguration().getResponse(requestSpecification, EndPoints.GET_USER_COMMITS, HttpStatus.SC_OK);
        List<Commit> commits = Arrays.asList(response.as(Commit[].class, ObjectMapperType.GSON));
    }

    @Test(groups = "23")
    public void commitTest2() {
        RequestSpecification requestSpecification = new RestAssuredConfiguration().getRequestSpecification();
        requestSpecification.accept(ContentType.JSON).pathParam("user", "axwtw").pathParam("repo", "fitbit-tests").pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c").log().all();
        Response response = new RestAssuredConfiguration().getResponse(requestSpecification, EndPoints.GET_USER_COMMIT_BY_SHA, HttpStatus.SC_OK);
        List<SingleCommit> singleCommits = Arrays.asList(response.as(SingleCommit[].class, ObjectMapperType.GSON));

    }
    @Test(groups = "demo")
    public void singleCommitAuthorValidation() {
        given()
                .pathParam("user", "axwtw")
                .pathParam("repo", "fitbit-tests")
                .pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c")
        .when()
                .get("https://api.github.com/repos/{user}/{repo}/commits/{sha}")
        .then()
                .statusCode(200)
                .body("commit.author.name", equalTo("Sergey Degtyaryov"))
        .and()
                .body("commit.author.email", equalTo("axwtww@gmail.com"));
    }

    @Test(groups = "demo")
    public void singleCommitComitterValidation() {
        given()
                .pathParam("user", "axwtw")
                .pathParam("repo", "fitbit-tests")
                .pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c")
        .when()
                .get("https://api.github.com/repos/{user}/{repo}/commits/{sha}")
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
                        .pathParam("user", "axwtw")
                        .pathParam("repo", "fitbit-tests")
                        .pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c")
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
                .pathParam("user", "axwtw")
                .pathParam("repo", "fitbit-tests")
                .pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c")
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
                .pathParam("user", "axwtw")
                .pathParam("repo", "fitbit-tests")
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
