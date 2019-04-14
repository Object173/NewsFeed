package com.object173.newsfeed.features.newslist.domain;

import com.object173.newsfeed.features.newslist.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface NewsRepository {
    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByFeed(String feedLink);
    DataSource.Factory<Integer, News> getNewsByCategory(String category);
    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);

    void setFeedUpdated(String feedLink, Date updated);
    int refreshNews(List<News> newsList, int cacheSize, Date cropDate);

    List<String> getFeedsById(String category);
}
