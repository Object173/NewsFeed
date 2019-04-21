package com.object173.newsfeed.features.feed.list.domain;

import com.object173.newsfeed.features.base.model.local.Feed;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedListRepository mFeedRepository;

    public FeedInteractorImpl(FeedListRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mFeedRepository.getLocalDataSource();
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource(String category) {
        return mFeedRepository.getFeedDataSource(category);
    }

    @Override
    public LiveData<Boolean> removeFeed(final String feedLink) {
        return mFeedRepository.removeFeed(feedLink);
    }
}