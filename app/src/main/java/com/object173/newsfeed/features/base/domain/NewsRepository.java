package com.object173.newsfeed.features.base.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.model.local.News;

public interface NewsRepository {
    LiveData<News> getNews(Long id);

    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByFeed(String feedLink);
    DataSource.Factory<Integer, News> getNewsByCategory(String category);

    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);
}
