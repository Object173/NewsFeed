package com.object173.newsfeed.features.newslist.domain;

import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface NewsRepository {
    DataSource.Factory<Integer, News> getLocalDataSource();
    DataSource.Factory<Integer, News> getNewsDataSource(String feedLink);
    LiveData<RequestResult> updateFeed(String feedLink);
    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);
}
