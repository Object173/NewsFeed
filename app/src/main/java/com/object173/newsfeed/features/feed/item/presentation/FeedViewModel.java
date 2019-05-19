package com.object173.newsfeed.features.feed.item.presentation;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.feed.item.domain.FeedInteractor;

class FeedViewModel extends ViewModel {

    private final FeedInteractor mFeedInteractor;
    private LiveData<RequestResult> mLoadStatus;

    FeedViewModel(FeedInteractor feedInteractor) {
        mFeedInteractor = feedInteractor;
    }

    LiveData<Feed> getFeed(String feedLink) {
        if(feedLink == null) {
            MutableLiveData<Feed> feed = new MutableLiveData<>();
            feed.setValue(new Feed());
            return feed;
        }
        return mFeedInteractor.getFeed(feedLink);
    }

    LiveData<RequestResult> loadFeed(Feed feed) {
        return mLoadStatus = mFeedInteractor.loadFeed(feed);
    }

    LiveData<RequestResult> updateFeed(Feed feed) {
        return mLoadStatus = mFeedInteractor.updateFeed(feed);
    }

    LiveData<RequestResult> getIsRefreshed() {
        return mLoadStatus;
    }
    void cancelLoadFeed(LifecycleOwner lifecycleOwner) {
        mLoadStatus.removeObservers(lifecycleOwner);
        mLoadStatus = null;
    }
}
