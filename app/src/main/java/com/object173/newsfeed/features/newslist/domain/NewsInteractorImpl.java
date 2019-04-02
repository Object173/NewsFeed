package com.object173.newsfeed.features.newslist.domain;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

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
        return mNewsRepository.getNewsDataSource();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource(String feedLink) {
        return mNewsRepository.getNewsDataSource(feedLink);
    }
}
