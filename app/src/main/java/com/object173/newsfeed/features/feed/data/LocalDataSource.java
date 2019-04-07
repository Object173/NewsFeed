package com.object173.newsfeed.features.feed.data;

import android.content.Context;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;


public interface LocalDataSource {
    LiveData<Feed> getFeed(String feedLink);

    boolean isExists(String feedLink);
    void insertFeed(FeedDB feed);
    void insertNews(String feedLink, List<NewsDB> newsList, int cacheSize, Date cropDate);

    LiveData<RequestResult> updateFeed(FeedDB feed);

    int getCacheSize(Context context);
    int getCacheFrequency(Context context);
}
