package com.object173.newsfeed.features.feed.list.presentation;

import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.feed.list.domain.FeedInteractor;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class FeedListFragmentViewModel extends BaseListFragmentViewModel<Feed> {

    private final FeedInteractor mInteractor;

    public FeedListFragmentViewModel(final FeedInteractor interactor) {
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
