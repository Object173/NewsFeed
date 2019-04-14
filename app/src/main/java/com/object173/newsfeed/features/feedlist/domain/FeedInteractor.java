package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface FeedInteractor {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);
    LiveData<Boolean> removeFeed(String feedLink);
}
