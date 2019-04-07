package com.object173.newsfeed.features.newslist.domain;

import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class NewsInteractorImpl implements NewsInteractor {

    private final NewsRepository mNewsRepository;

    public NewsInteractorImpl(NewsRepository newsRepository) {
        mNewsRepository = newsRepository;
    }

    @Override
    public LiveData<RequestResult> updateFeed(final String feedLink) {
        return mNewsRepository.updateFeed(feedLink);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource() {
        return mNewsRepository.getLocalDataSource();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource(String feedLink) {
        return mNewsRepository.getNewsDataSource(feedLink);
    }

    @Override
    public LiveData<Boolean> hideNews(long id) {
        return mNewsRepository.hideNews(id);
    }

    @Override
    public void checkReviewed(long id) {
        mNewsRepository.checkReviewed(id);
    }
}
