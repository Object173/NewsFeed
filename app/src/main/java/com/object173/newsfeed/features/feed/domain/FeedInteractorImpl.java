package com.object173.newsfeed.features.feed.domain;

import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import androidx.lifecycle.LiveData;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedRepository mFeedRepository;

    public FeedInteractorImpl(FeedRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public LiveData<Feed> getFeed(String feedLink) {
        return mFeedRepository.getFeed(feedLink);
    }

    @Override
    public LiveData<RequestResult> loadFeed(final Feed feed) {
        return mFeedRepository.loadFeed(feed);
    }

    @Override
    public LiveData<RequestResult> updateFeed(Feed feed) {
        return mFeedRepository.updateFeed(feed);
    }
}
