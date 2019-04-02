package com.object173.newsfeed.features.newslist.domain;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

public interface NewsInteractor {
    LiveData<RequestResult> updateFeed(String feedLink);
    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsDataSource(String feedLink);
}
