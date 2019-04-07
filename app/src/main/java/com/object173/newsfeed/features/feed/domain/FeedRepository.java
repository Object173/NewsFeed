package com.object173.newsfeed.features.feed.domain;

import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import androidx.lifecycle.LiveData;

public interface FeedRepository {
    LiveData<Feed> getFeed(String feedLink);
    LiveData<RequestResult> loadFeed(Feed feed);
    LiveData<RequestResult> updateFeed(Feed feed);
}
