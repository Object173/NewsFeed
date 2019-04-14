package com.object173.newsfeed.features.newslist.data;

import android.content.Context;

import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.paging.DataSource;

public interface LocalDataSource {
    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsByFeed(String feedLink);
    DataSource.Factory<Integer, News> getNewsByCategory(String category);

    boolean isNewsExist(String feedLink, Date pubDate);

    int hideNews(long id);
    void checkReviewed(long id);

    void setFeedUpdated(String feedLink, Date updated);
    int refreshNews(List<NewsDB> newsList, int cacheSize, Date cropDate);

    List<String> getFeedsByCategory(String category);
}
