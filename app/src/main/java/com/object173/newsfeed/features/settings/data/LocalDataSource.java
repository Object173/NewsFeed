package com.object173.newsfeed.features.settings.data;

import android.content.Context;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;

import java.util.Date;
import java.util.List;

public interface LocalDataSource {
    List<FeedDB> getAutoUpdatedFeeds();

    boolean isNewsExist(String feedLink, Date pubDate);
    void setFeedUpdated(String feedLink, Date updated);

    long refreshNews(String feedLink, List<NewsDB> newsList, int cacheSize, Date cropDate);
}
