package com.object173.newsfeed.features.feedlist.domain;

import com.object173.newsfeed.features.feedlist.domain.model.Category;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface FeedRepository {
    DataSource.Factory<Integer, Feed> getLocalDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);
    LiveData<Boolean> removeFeed(String feedLink);

    LiveData<List<Category>> getCategories();
}
