package com.object173.newsfeed.features.news.data;
import android.arch.lifecycle.LiveData;

import com.object173.newsfeed.features.news.domain.NewsRepository;
import com.object173.newsfeed.features.news.domain.model.News;

public class NewsRepositoryImpl implements NewsRepository {

    private final NewsDataSource mNewsDataSource;

    public NewsRepositoryImpl(NewsDataSource newsDataSource) {
        mNewsDataSource = newsDataSource;
    }

    @Override
    public LiveData<News> getNews(final Long id) {
        return mNewsDataSource.getNews(id);
    }
}
