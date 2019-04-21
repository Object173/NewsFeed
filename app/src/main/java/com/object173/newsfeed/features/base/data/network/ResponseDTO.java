package com.object173.newsfeed.features.base.data.network;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import java.util.List;

public class ResponseDTO {

    public final RequestResult result;
    public final Feed feed;
    public final List<News> newsList;

    private ResponseDTO(RequestResult result, Feed feed, List<News> newsList) {
        this.result = result;
        this.feed = feed;
        this.newsList = newsList;
    }

    public static ResponseDTO createFail(RequestResult result) {
        return new ResponseDTO(result, null, null);
    }

    public static ResponseDTO createSuccess(Feed feed, List<News> newsList) {
        return new ResponseDTO(RequestResult.SUCCESS, feed, newsList);
    }
}
