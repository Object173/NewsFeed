package com.object173.newsfeed.features.feed.item.domain;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import androidx.lifecycle.LiveData;

public interface FeedInteractor {
    LiveData<Feed> getFeed(String feedLink);
    LiveData<RequestResult> loadFeed(Feed feed);
    LiveData<RequestResult> updateFeed(Feed feed);
}
