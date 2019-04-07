package com.object173.newsfeed.features.feedlist.data;

import com.object173.newsfeed.features.feedlist.domain.FeedRepository;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class FeedRepositoryImpl implements FeedRepository {

    private final FeedDataSource mFeedDataSource;

    public FeedRepositoryImpl(FeedDataSource feedDataSource) {
        mFeedDataSource = feedDataSource;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mFeedDataSource.getFeedDataSource();
    }

    @Override
    public LiveData<Boolean> removeFeed(final String feedLink) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> {
            result.postValue(mFeedDataSource.removeFeed(feedLink) > 0);
        }).start();
        return result;
    }
}
