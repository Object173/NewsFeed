package com.object173.newsfeed.features.news.domain;

import com.object173.newsfeed.features.news.domain.model.News;

import androidx.lifecycle.LiveData;

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
