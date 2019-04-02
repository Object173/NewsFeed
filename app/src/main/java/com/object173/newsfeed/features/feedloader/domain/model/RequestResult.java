package com.object173.newsfeed.features.feedloader.domain.model;

public enum RequestResult {
    RUNNING,
    HTTP_FAIL,
    INCORRECT_LINK,
    INCORRECT_RESPONSE,
    EXISTS,
    FAIL,
    SUCCESS
}
