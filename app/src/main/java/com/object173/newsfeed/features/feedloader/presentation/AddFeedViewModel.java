package com.object173.newsfeed.features.feedloader.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.object173.newsfeed.features.feedloader.domain.LoaderInteractor;
import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;

class AddFeedViewModel extends ViewModel {

    private final LoaderInteractor mLoaderInteractor;
    private LiveData<RequestResult> mLoadStatus;

    AddFeedViewModel(final LoaderInteractor loaderInteractor) {
        mLoaderInteractor = loaderInteractor;
    }

    LiveData<RequestResult> loadFeed(String feedLink) {
        mLoadStatus = mLoaderInteractor.loadFeed(feedLink);
        return mLoadStatus;
    }

    LiveData<RequestResult> getIsRefreshed() {
        return mLoadStatus;
    }

    void cancelLoadFeed() {
        mLoadStatus = null;
    }
}
