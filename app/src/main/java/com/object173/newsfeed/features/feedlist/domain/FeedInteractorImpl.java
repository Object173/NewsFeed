package com.object173.newsfeed.features.feedlist.domain;

import android.arch.paging.DataSource;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedRepository mFeedRepository;

    public FeedInteractorImpl(FeedRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mFeedRepository.getFeedDataSource();
    }
}