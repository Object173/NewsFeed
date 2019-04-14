package com.object173.newsfeed.features.feed.presentation;

import com.object173.newsfeed.features.feed.device.DownloadWorker;
import com.object173.newsfeed.features.feed.domain.FeedInteractor;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class FeedViewModel extends ViewModel {

    private final FeedInteractor mFeedInteractor;

    private final LiveData<Feed> mFeed;
    private LiveData<RequestResult> mLoadStatus;

    FeedViewModel(FeedInteractor feedInteractor, String feedLink) {
        mFeedInteractor = feedInteractor;

        if(feedLink != null) {
            mFeed = mFeedInteractor.getFeed(feedLink);
        }
        else {
            mFeed = new MutableLiveData<>();
            ((MutableLiveData<Feed>) mFeed).setValue(new Feed());
        }
    }

    LiveData<Feed> getFeed() {
        return mFeed;
    }

    LiveData<RequestResult> loadFeed() {
        return mLoadStatus = DownloadWorker.startLoadFeed(mFeed.getValue());
    }

    LiveData<RequestResult> updateFeed() {
        return mLoadStatus = mFeedInteractor.updateFeed(mFeed.getValue());
    }

    LiveData<RequestResult> getIsRefreshed() {
        return mLoadStatus;
    }
    void cancelLoadFeed(LifecycleOwner lifecycleOwner) {
        mLoadStatus.removeObservers(lifecycleOwner);
        mLoadStatus = null;
    }
}
