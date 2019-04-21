package com.object173.newsfeed.features.base.domain;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.model.local.Feed;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;

import java.util.List;

public interface FeedRepository {
    LiveData<Feed> getFeed(String feedLink);

    DataSource.Factory<Integer, Feed> getLocalDataSource();
    DataSource.Factory<Integer, Feed> getFeedDataSource(String category);

    List<String> getAutoUpdateList();

    LiveData<Boolean> updateFeed(Feed feed);
    LiveData<RequestResult> insertFeed(Feed feed);
    LiveData<RequestResult> updateFeedNewsAsync(String feedLink);
    LiveData<RequestResult> updateCategoryAsync(String category);
    Pair<RequestResult, Integer> updateFeedNews(String feedLink);
    LiveData<Boolean> removeFeed(String feedLink);
}

