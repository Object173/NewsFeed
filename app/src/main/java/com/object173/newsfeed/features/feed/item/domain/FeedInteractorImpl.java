package com.object173.newsfeed.features.feed.item.domain;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedItemRepository mFeedRepository;

    public FeedInteractorImpl(FeedItemRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public LiveData<Feed> getFeed(String feedLink) {
        return mFeedRepository.getFeed(feedLink);
    }

    @Override
    public LiveData<RequestResult> loadFeed(Feed feed) {
        return mFeedRepository.insertFeed(feed);
    }

    @Override
    public LiveData<RequestResult> updateFeed(Feed feed) {
        return Transformations.map(mFeedRepository.updateFeed(feed), result -> {
            if(result == null) {
                return RequestResult.RUNNING;
            }
            return result ? RequestResult.SUCCESS : RequestResult.FAIL;
        });
    }
}
