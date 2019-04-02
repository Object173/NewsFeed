package com.object173.newsfeed.features.feedlist.domain;

import android.arch.paging.DataSource;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;

public interface FeedRepository {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
}
