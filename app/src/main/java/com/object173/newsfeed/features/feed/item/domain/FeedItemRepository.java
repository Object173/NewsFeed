package com.object173.newsfeed.features.feed.item.domain;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import java.util.List;

public interface FeedItemRepository {
    LiveData<Feed> getFeed(String feedLink);

    List<String> getAutoUpdateList();

    LiveData<Boolean> updateFeed(Feed feed);
    LiveData<RequestResult> insertFeed(Feed feed);
}

