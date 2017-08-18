package com.github.test.common;


public interface EndPoints {

    String GET_USER = "/users/{user}";
    String GET_FOLLOWERS = "/followers";
    String GET_USER_FOLLOWERS = "/users/{user}/followers";
    String GET_USER_COMMITS = "/repos/{user}/{repo}/commits";
    String GET_USER_COMMIT_BY_SHA = "https://api.github.com/repos/{user}/{repo}/commits/{sha}";
    String GET_USER_COMMIT_COMMENTS_URL = "https://api.github.com/repos/axwtw/fitbit-tests/commits/fd819ca846dae2d2898fc386029dbbc5c4cf706c/comments";
    String GITHUB_AUTHORIZATIONS_URL = "https://api.github.com/authorizations";
    String GITHUB_DELETE_COMMENT_URL = "https://api.github.com/repos/{user}/{repo}/comments/{id}";
}
