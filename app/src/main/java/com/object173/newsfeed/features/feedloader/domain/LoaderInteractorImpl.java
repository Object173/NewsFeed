package com.object173.newsfeed.features.feedloader.domain;

import android.arch.lifecycle.LiveData;

import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;

public class LoaderInteractorImpl implements LoaderInteractor {

    private final LoaderRepository mRepository;

    public LoaderInteractorImpl(LoaderRepository repository) {
        mRepository = repository;
    }

    @Override
    public LiveData<RequestResult> loadFeed(final String feedLink) {
        return mRepository.loadFeed(feedLink);
    }
}
