package com.object173.newsfeed.features.feedloader.domain;

import android.arch.lifecycle.LiveData;

import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;

public interface LoaderRepository {
    LiveData<RequestResult> loadFeed(String feedLink);
}
