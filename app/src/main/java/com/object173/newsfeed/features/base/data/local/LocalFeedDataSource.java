package com.object173.newsfeed.features.base.data.local;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.model.local.Feed;

public interface LocalFeedDataSource {
    LiveData<Feed> getFeedAsync(String feedLink);
    Feed getFeed(String feedLink);

    DataSource.Factory<Integer, Feed> getFeedDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);

    List<String> getFeedsByCategory(String category);
    List<String> getAutoUpdatedList();

    boolean insertFeed(Feed feed);
    boolean updateFeed(Feed feed);
    boolean removeFeed(String feedLink);
    boolean isExist(String feedLink);
}
