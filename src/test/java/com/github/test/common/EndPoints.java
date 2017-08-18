package com.github.test.common;


public interface EndPoints {

    String GET_USER = "/users/{user}";
    String GET_FOLLOWERS = "/followers";
    String GET_USER_FOLLOWERS = "/users/{user}/followers";
    String GET_USER_COMMITS = "/repos/{user}/{repo}/commits";
    String GET_USER_COMMIT_BY_SHA = "/repos/{user}/{repo}/commits/{sha}";
}
