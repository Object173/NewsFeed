package com.object173.newsfeed.features.settings.data;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;

import java.util.Date;
import java.util.List;

public interface LocalDataSource {
    List<FeedDB> getAutoUpdatedFeeds();

    void setFeedUpdated(String feedLink, Date updated);
    int refreshNews(List<NewsDB> newsList, int cacheSize, Date cropDate);
}
