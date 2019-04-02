package com.object173.newsfeed.libs.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloaderImpl<T> implements Downloader {

    private final ResponseParser<T> responseParser;
    private final int readTimeout;
    private final int connectTimeout;

    DownloaderImpl(ResponseParser<T> responseParser, int readTimeout, int connectTimeout) {
        this.responseParser = responseParser;
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    @Override
    public Response downloadObject(final String url) {
        HttpURLConnection connection = null;
        try {
            try {
                connection = openConnection(url);
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return Response.getHttpError(Response.Result.HTTP_ERROR, connection.getResponseCode());
                }
            } catch (IOException e) {
                return Response.getError(Response.Result.CONNECTION_ERROR);
            }

            final InputStream inStream;
            try {
                inStream = new BufferedInputStream(connection.getInputStream());
            } catch (IOException e) {
                return Response.getError(Response.Result.CONNECTION_ERROR);
            }

            try {
                final T parsedObject = responseParser.parse(inStream);
                return Response.getSuccess(parsedObject);
            } catch (IOException e) {
                return Response.getError(Response.Result.PARSE_ERROR);
            }
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection openConnection(final String feedLink) throws IOException {
        final URL feedUrl = new URL(feedLink);
        final HttpURLConnection connection  = (HttpURLConnection) feedUrl.openConnection();
        connection.setReadTimeout(readTimeout);
        connection.setConnectTimeout(connectTimeout);
        connection.connect();
        return connection;
    }
}
