package com.object173.newsfeed.features.feedloader.domain;

import android.arch.lifecycle.LiveData;

import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;

public interface LoaderInteractor {
    LiveData<RequestResult> loadFeed(String feedLink);
}
