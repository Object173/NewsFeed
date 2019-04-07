package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedRepository mFeedRepository;

    public FeedInteractorImpl(FeedRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mFeedRepository.getFeedDataSource();
    }

    @Override
    public LiveData<Boolean> removeFeed(final String feedLink) {
        return mFeedRepository.removeFeed(feedLink);
    }
}