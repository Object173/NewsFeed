package com.object173.newsfeed.features.news.item.domain;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.base.model.local.News;

public class NewsInteractorImpl implements NewsInteractor {

    private final NewsItemRepository mNewsRepository;

    public NewsInteractorImpl(NewsItemRepository newsRepository) {
        mNewsRepository = newsRepository;
    }

    @Override
    public LiveData<News> getNews(final Long newsId) {
        return mNewsRepository.getNews(newsId);
    }
}
