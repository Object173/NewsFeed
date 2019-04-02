package com.object173.newsfeed.features.newslist.data;

import android.arch.paging.DataSource;

import com.object173.newsfeed.features.newslist.domain.model.News;

public interface NewsDataSource {
    DataSource.Factory<Integer, News> getNewsDataSource();
    DataSource.Factory<Integer, News> getNewsDataSource(String feedLink);
}
