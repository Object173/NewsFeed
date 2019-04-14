package com.object173.newsfeed.features.feedlist.data;

import com.object173.newsfeed.features.feedlist.domain.model.Category;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface LocalDataSource {
    DataSource.Factory<Integer, Feed> getFeedDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);
    int removeFeed(String feedLink);

    LiveData<List<Category>> getCategories();
}
