package com.object173.newsfeed.features.news.list.category.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import java.util.List;

public interface NewsCategoryRepository {
    LiveData<News> getNews(Long id);

    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByCategory(String category);

    LiveData<RequestResult> updateCategoryAsync(String category);

    LiveData<Boolean> hideNews(long id);
    void checkReviewed(List<News> reviewedList);
}
