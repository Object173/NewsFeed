package com.object173.newsfeed.features.feedlist.data;

import com.object173.newsfeed.features.feedlist.domain.FeedRepository;
import com.object173.newsfeed.features.feedlist.domain.model.Category;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class FeedRepositoryImpl implements FeedRepository {

    private final LocalDataSource mLocalDataSource;

    public FeedRepositoryImpl(LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getLocalDataSource() {
        return mLocalDataSource.getFeedDataSource();
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource(String category) {
        return mLocalDataSource.getFeedDataSource(category);
    }

    @Override
    public LiveData<Boolean> removeFeed(final String feedLink) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> result.postValue(mLocalDataSource.removeFeed(feedLink) > 0)).start();
        return result;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return mLocalDataSource.getCategories();
    }
}
