package com.object173.newsfeed.features.feedlist.data;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import androidx.paging.DataSource;

public interface FeedDataSource {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
    int removeFeed(String feedLink);
}
