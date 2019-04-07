package com.object173.newsfeed.features.newslist.domain;

import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface NewsInteractor {
    LiveData<RequestResult> updateFeed(String feedLink);
    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsDataSource(String feedLink);
    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);
}
