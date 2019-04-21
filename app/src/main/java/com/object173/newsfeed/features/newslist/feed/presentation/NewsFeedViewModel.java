package com.object173.newsfeed.features.newslist.feed.presentation;

import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.newslist.feed.domain.NewsFeedInteractor;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class NewsFeedViewModel extends BaseListFragmentViewModel<News> {

    private final NewsFeedInteractor mNewsInteractor;

    public NewsFeedViewModel(final NewsFeedInteractor newsInteractor) {
        mNewsInteractor = newsInteractor;
    }

    @Override
    protected DataSource.Factory<Integer, News> loadFactoryData() {
        return mNewsInteractor.getAllNews();
    }

    @Override
    protected DataSource.Factory<Integer, News> loadFactoryData(String param) {
        return mNewsInteractor.getNewsByFeed(param);
    }

    @Override
    protected LiveData<Boolean> removeData(News obj) {
        return mNewsInteractor.hideNews(obj.getId());
    }

    @Override
    protected LiveData<RequestResult> refreshData() {
        return mNewsInteractor.updateFeedNews(mParam);
    }

    @Override
    protected boolean isRefreshEnabled() {
        return true;
    }
}
