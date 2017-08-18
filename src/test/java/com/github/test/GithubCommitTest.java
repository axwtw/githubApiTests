package com.github.test;

import com.github.framework.RestAssuredConfiguration;
import com.github.test.bin.Commit;
import com.github.test.bin2.SingleCommit;
import com.github.test.common.EndPoints;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by sergey on 8/17/17.
 */
public class GithubCommitTest {

    @Test(groups = "demo")
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
    @Test
    public void singleCommitAuthorValidation() {
        given().pathParam("user", "axwtw").pathParam("repo", "fitbit-tests").pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c")
                .when().get("https://api.github.com/repos/{user}/{repo}/commits/{sha}")
                .then().statusCode(200).body("commit.author.name", equalTo("Sergey Degtyaryov")).and().body("commit.author.email", equalTo("axwtww@gmail.com"));
    }

    @Test
    public void singleCommitComitterValidation() {
        given().pathParam("user", "axwtw").pathParam("repo", "fitbit-tests").pathParam("sha", "fd819ca846dae2d2898fc386029dbbc5c4cf706c")
                .when().get("https://api.github.com/repos/{user}/{repo}/commits/{sha}")
                .then().statusCode(200).body("commit.committer.name", equalTo("Sergey Degtyaryov")).and().body("commit.comitter.email", equalTo("axwtww@gmail.com"));
    }


    //818af8a09936fa27cd89f1bf393e27e3a30d92b7 mwthr token
    @Test
    public void addNewCommentToSingleCommit() {
        Map<String, Object> jsonAsMap = new HashMap<String, Object>();
        jsonAsMap.put("body", "test comment");
        jsonAsMap.put("path", "");
        jsonAsMap.put("position", "4");
        jsonAsMap.put("line", "null");


        given().accept(ContentType.JSON).contentType(ContentType.JSON).header("Authorization", "token 818af8a09936fa27cd89f1bf393e27e3a30d92b7").body(jsonAsMap)
                .when().post("https://api.github.com/repos/axwtw/fitbit-tests/commits/fd819ca846dae2d2898fc386029dbbc5c4cf706c/comments")
                .then().statusCode(201).log().all();
    }

}
