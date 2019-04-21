package com.object173.newsfeed.features.newslist.feed.domain;

import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.NewsRepository;
import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class NewsFeedInteractorImpl implements NewsFeedInteractor {

    private final NewsRepository mRepository;
    private final FeedRepository mFeedRepository;

    public NewsFeedInteractorImpl(NewsRepository repository, FeedRepository feedRepository) {
        mRepository = repository;
        mFeedRepository = feedRepository;
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
    public void checkReviewed(long id) {
        mRepository.checkReviewed(id);
    }

    @Override
    public LiveData<RequestResult> updateFeedNews(String feedLink) {
        return mFeedRepository.updateFeedNewsAsync(feedLink);
    }
}
