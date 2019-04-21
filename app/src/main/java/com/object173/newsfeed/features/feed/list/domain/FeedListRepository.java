package com.object173.newsfeed.features.feed.list.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.Feed;

public interface FeedListRepository {
    LiveData<Feed> getFeed(String feedLink);

    DataSource.Factory<Integer, Feed> getLocalDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);

    LiveData<Boolean> updateFeed(Feed feed);
    LiveData<Boolean> removeFeed(String feedLink);
}

