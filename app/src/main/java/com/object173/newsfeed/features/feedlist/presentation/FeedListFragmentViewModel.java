package com.object173.newsfeed.features.feedlist.presentation;

import com.object173.newsfeed.features.feedlist.domain.FeedInteractor;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

class FeedListFragmentViewModel extends ViewModel {

    private static final int LOAD_BLOCK_SIZE = 10;
    private static final int PREFETCH_DISTANCE = 5;

    private final FeedInteractor mInteractor;
    private LiveData<PagedList<Feed>> mFeedData;
    private String mCategory;

    FeedListFragmentViewModel(final FeedInteractor interactor) {
        mInteractor = interactor;
    }

    private LiveData<PagedList<Feed>> getFeedData(DataSource.Factory<Integer, Feed> factory) {
        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(LOAD_BLOCK_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        mFeedData = new LivePagedListBuilder<>(
                factory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();

        return mFeedData;
    }

    LiveData<PagedList<Feed>> getFeedData(String category) {
        if(mCategory != null && mCategory.equals(category) && mFeedData != null) {
            return mFeedData;
        }
        mCategory = category;
        return getFeedData(mInteractor.getFeedDataSource(mCategory));
    }

    LiveData<PagedList<Feed>> getFeedData() {
        if(mCategory == null && mFeedData != null) {
            return mFeedData;
        }
        mCategory = null;
        return getFeedData(mInteractor.getFeedDataSource());
    }

    LiveData<Boolean> removeFeed(final int position) {
        return mInteractor.removeFeed(mFeedData.getValue().get(position).getLink());
    }
}
