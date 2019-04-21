package com.object173.newsfeed.features.news.domain;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.base.domain.NewsRepository;
import com.object173.newsfeed.features.base.domain.model.local.News;

public class NewsInteractorImpl implements NewsInteractor {

    private final NewsRepository mNewsRepository;

    public NewsInteractorImpl(NewsRepository newsRepository) {
        mNewsRepository = newsRepository;
    }

    @Override
    public LiveData<News> getNews(final Long newsId) {
        return mNewsRepository.getNews(newsId);
    }
}
