package com.object173.newsfeed.features.feedlist.presentation;

import com.object173.newsfeed.features.base.domain.model.local.Feed;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractor;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

class FeedListFragmentViewModel extends BaseListFragmentViewModel<Feed> {

    private final FeedInteractor mInteractor;

    FeedListFragmentViewModel(final FeedInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    protected DataSource.Factory<Integer, Feed> loadFactoryData(String param) {
        return mInteractor.getFeedDataSource(param);
    }

    @Override
    protected DataSource.Factory<Integer, Feed> loadFactoryData() {
        return mInteractor.getFeedDataSource();
    }

    @Override
    protected LiveData<Boolean> removeData(Feed obj) {
        return mInteractor.removeFeed(obj.getLink());
    }
}
