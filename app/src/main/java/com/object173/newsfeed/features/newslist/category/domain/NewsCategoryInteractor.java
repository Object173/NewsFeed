package com.object173.newsfeed.features.newslist.category.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;

public interface NewsCategoryInteractor {
    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByCategory(String category);

    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);

    LiveData<RequestResult> updateCategory(String category);
}
