package com.object173.newsfeed.libs.network;

public interface Downloader<T> {
    Response<T> downloadObject(final String url);
}
