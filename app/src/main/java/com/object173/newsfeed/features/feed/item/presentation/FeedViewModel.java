package com.object173.newsfeed.features.feed.item.presentation;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.feed.item.domain.FeedInteractor;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

class FeedViewModel extends ViewModel {

    private final FeedInteractor mFeedInteractor;

    private final LiveData<Feed> mFeed;
    private LiveData<RequestResult> mLoadStatus;

    private boolean mIsNewFeed;

    FeedViewModel(FeedInteractor feedInteractor, String feedLink) {
        mFeedInteractor = feedInteractor;

        if(feedLink != null) {
            mFeed = Transformations.map(mFeedInteractor.getFeed(feedLink), feed -> {
                mIsNewFeed = (feed == null);
                if(feed == null) {
                    Feed newFeed = new Feed();
                    newFeed.setLink(feedLink);
                    return newFeed;
                }
                return feed;
            });
        }
        else {
            mFeed = new MutableLiveData<>();
            ((MutableLiveData<Feed>) mFeed).setValue(new Feed());
            mIsNewFeed = true;
        }
    }

    LiveData<Feed> getFeed() {
        return mFeed;
    }

    LiveData<RequestResult> loadFeed() {
        return mLoadStatus = mIsNewFeed ? mFeedInteractor.loadFeed(mFeed.getValue()) :
                mFeedInteractor.updateFeed(mFeed.getValue());
    }

    LiveData<RequestResult> getIsRefreshed() {
        return mLoadStatus;
    }
    void cancelLoadFeed(LifecycleOwner lifecycleOwner) {
        mLoadStatus.removeObservers(lifecycleOwner);
        mLoadStatus = null;
    }

    public boolean isNewFeed() {
        return mIsNewFeed;
    }
}
