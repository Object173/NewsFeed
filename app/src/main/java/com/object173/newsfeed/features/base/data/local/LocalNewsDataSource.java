package com.object173.newsfeed.features.base.data.local;


import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.News;

public interface LocalNewsDataSource {
    LiveData<News> getNews(Long id);

    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsByFeed(String feedLink);
    DataSource.Factory<Integer, News> getNewsByCategory(String category);

    int insertNews(List<News> newsList, int cacheSize, Date cropDate);
    boolean isNewsExist(String feedLink, Date pubDate);
    boolean hideNews(long id);
    void checkReviewed(long id);
}
