package com.object173.newsfeed.features.news.list.feed.domain;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import java.util.List;

public class NewsFeedInteractorImpl implements NewsFeedInteractor {

    private final NewsFeedRepository mRepository;

    public NewsFeedInteractorImpl(NewsFeedRepository repository) {
        mRepository = repository;
    }

    @Override
    public DataSource.Factory<Integer, News> getAllNews() {
        return mRepository.getAllNews();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByFeed(String feedLink) {
        return mRepository.getNewsByFeed(feedLink);
    }

    @Override
    public LiveData<Boolean> hideNews(long id) {
        return mRepository.hideNews(id);
    }

    @Override
    public void checkReviewed(List<News> reviewedList) {
        mRepository.checkReviewed(reviewedList);
    }

    @Override
    public LiveData<RequestResult> updateFeedNews(String feedLink) {
        return mRepository.updateFeedNewsAsync(feedLink);
    }
}
