package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface FeedRepository {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
    LiveData<Boolean> removeFeed(String feedLink);
}
