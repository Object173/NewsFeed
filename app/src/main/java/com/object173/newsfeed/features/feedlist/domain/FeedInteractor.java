package com.object173.newsfeed.features.feedlist.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.model.local.Feed;

public interface FeedInteractor {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);
    LiveData<Boolean> removeFeed(String feedLink);
}
