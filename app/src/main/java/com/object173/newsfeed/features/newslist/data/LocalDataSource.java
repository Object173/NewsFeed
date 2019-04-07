package com.object173.newsfeed.features.newslist.data;

import android.content.Context;

import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.paging.DataSource;

public interface LocalDataSource {
    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsDataSource(String feedLink);

    boolean isExist(String feedLink, Date pubDate);

    int hideNews(long id);
    void checkReviewed(long id);

    boolean isFeedExists(String feedLink);
    void setFeedUpdated(String feedLink, Date updated);

    long refreshNews(String feedLink, List<NewsDB> newsList, int cacheSize, Date cropDate);

    int getCacheSize(Context context);
    int getCacheFrequency(Context context);
}
