package com.object173.newsfeed.features.newslist.domain;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

public interface NewsRepository {
    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsDataSource(String feedLink);
    LiveData<RequestResult> updateFeed(String feedLink);
}
