package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.base.domain.CategoryRepository;
import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.model.local.Feed;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedRepository mFeedRepository;

    public FeedInteractorImpl(FeedRepository feedRepository) {
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