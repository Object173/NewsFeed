package com.object173.newsfeed.libs.network;

import java.net.HttpURLConnection;

public class Response<T> {

    public enum Result {
        Success,
        CONNECTION_ERROR,
        HTTP_ERROR,
        PARSE_ERROR
    }

    public final Result result;
    public final int httpCode;
    public final T body;

    private Response(final Result result, final int httpCode, final T body) {
        this.result = result;
        this.httpCode = httpCode;
        this.body = body;
    }

    static <T>Response getSuccess(final T body) {
        return new Response<T>(Result.Success, HttpURLConnection.HTTP_OK, body);
    }

    static <T>Response getError(final Result result) {
        return new Response<>(result, 0, null);
    }

    static <T>Response getHttpError(final Result result, final int httpCode) {
        return new Response<>(result, httpCode, null);
    }
}
