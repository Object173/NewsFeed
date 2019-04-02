package com.object173.newsfeed.features.feedlist.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.object173.newsfeed.features.feedlist.domain.FeedInteractor;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import java.util.concurrent.Executors;

class FeedListViewModel extends ViewModel {

    private static final int LOAD_BLOCK_SIZE = 10;
    private static final int PREFETCH_DISTANCE = 10;

    private final FeedInteractor mInteractor;
    private LiveData<PagedList<Feed>> mFeedData;

    FeedListViewModel(final FeedInteractor interactor) {
        mInteractor = interactor;

        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(LOAD_BLOCK_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        mFeedData = new LivePagedListBuilder<>(
                interactor.getFeedDataSource(), config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    LiveData<PagedList<Feed>> getFeedData() {
        return mFeedData;
    }
}
