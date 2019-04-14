package com.object173.newsfeed.features.feed.data;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.features.feed.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;


public interface LocalDataSource {
    LiveData<Feed> getFeed(String feedLink);

    boolean insertFeed(Feed feed);
    void insertNews(List<News> newsList, int cacheSize, Date cropDate);

    LiveData<RequestResult> updateFeed(Feed feed);
}
