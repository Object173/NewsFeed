package com.object173.newsfeed.features.news.item.data;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.news.item.domain.NewsItemRepository;
import com.object173.newsfeed.features.base.model.local.News;

public class NewsItemRepositoryImpl implements NewsItemRepository {

    private final LocalNewsDataSource mNewsDataSource;

    public NewsItemRepositoryImpl(LocalNewsDataSource newsDataSource) {
        mNewsDataSource = newsDataSource;
    }

    @Override
    public LiveData<News> getNews(Long id) {
        return mNewsDataSource.getNews(id);
    }
}
