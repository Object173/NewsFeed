package com.object173.newsfeed.features.settings.data;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;

import java.util.Date;
import java.util.List;

public class LocalDataSourceImpl implements LocalDataSource {

    private final AppDatabase mDatabase;

    LocalDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public List<FeedDB> getAutoUpdatedFeeds() {
        return mDatabase.feedDao().getAutoUpdated();
    }

    @Override
    public boolean isNewsExist(String feedLink, Date pubDate) {
        return mDatabase.newsDao().isExist(feedLink, pubDate) > 0;
    }

    @Override
    public void setFeedUpdated(String feedLink, Date updated) {
        mDatabase.feedDao().setUpdated(feedLink, updated);
    }

    @Override
    public long refreshNews(String feedLink, List<NewsDB> newsList, int cacheSize, Date cropDate) {
        long count = mDatabase.newsDao().insert(newsList).length;
        mDatabase.newsDao().cropDate(feedLink, cropDate);
        mDatabase.newsDao().cropCount(feedLink, cacheSize);

        return count;
    }
}
