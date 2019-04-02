package com.object173.newsfeed.features.feedlist.data;
import android.arch.paging.DataSource;

import com.object173.newsfeed.features.feedlist.domain.FeedRepository;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

public class FeedRepositoryImpl implements FeedRepository {

    private final FeedDataSource mFeedDataSource;

    public FeedRepositoryImpl(FeedDataSource feedDataSource) {
        mFeedDataSource = feedDataSource;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mFeedDataSource.getFeedDataSource();
    }
}
