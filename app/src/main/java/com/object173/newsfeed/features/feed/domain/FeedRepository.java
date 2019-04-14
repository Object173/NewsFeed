package com.object173.newsfeed.features.feed.domain;

import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.features.feed.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public interface FeedRepository {
    LiveData<Feed> getFeed(String feedLink);
    LiveData<RequestResult> updateFeed(Feed feed);

    boolean insertFeed(Feed feed);
    void insertNews(List<News> newsList, int cacheSize, Date cropDate);
}
