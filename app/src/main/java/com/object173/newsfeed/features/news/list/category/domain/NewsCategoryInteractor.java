package com.object173.newsfeed.features.news.list.category.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

public interface NewsCategoryInteractor {
    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByCategory(String category);

    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);

    LiveData<RequestResult> updateCategory(String category);
}
