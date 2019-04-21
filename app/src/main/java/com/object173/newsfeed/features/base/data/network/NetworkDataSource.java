package com.object173.newsfeed.features.base.data.network;

public interface NetworkDataSource {
    ResponseDTO getNewsFeed(String feedLink);
}
