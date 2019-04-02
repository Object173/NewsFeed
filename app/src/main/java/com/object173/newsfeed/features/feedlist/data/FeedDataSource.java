package com.object173.newsfeed.features.feedlist.data;

import android.arch.paging.DataSource;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

public interface FeedDataSource {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
}
