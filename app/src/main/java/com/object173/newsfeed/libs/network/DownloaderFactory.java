package com.object173.newsfeed.libs.network;

public class DownloaderFactory {

    private static final int READ_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 3000;

    public static <T>Downloader get(ResponseParser<T> responseParser) {
        return new DownloaderImpl<>(responseParser, READ_TIMEOUT, CONNECT_TIMEOUT);
    }
}
