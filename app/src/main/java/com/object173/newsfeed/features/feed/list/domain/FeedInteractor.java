package com.object173.newsfeed.features.feed.list.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.Feed;

public interface FeedInteractor {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);
    LiveData<Boolean> removeFeed(String feedLink);
}
