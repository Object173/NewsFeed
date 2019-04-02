package com.object173.newsfeed.features.newslist.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.object173.newsfeed.features.newslist.domain.NewsInteractor;
import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import java.util.concurrent.Executors;

class NewsListViewModel extends ViewModel {

    private static final int LOAD_BLOCK_SIZE = 10;
    private static final int PREFETCH_DISTANCE = 10;

    private final NewsInteractor mNewsInteractor;

    private final String mFeedLink;
    private LiveData<PagedList<News>> mNewsData;
    private LiveData<RequestResult> mRequestResult;

    NewsListViewModel(final NewsInteractor newsInteractor, final String feedLink) {
        mNewsInteractor = newsInteractor;
        mFeedLink = feedLink;

        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(LOAD_BLOCK_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        final DataSource.Factory<Integer, News> dataSourceFactory = (mFeedLink == null) ?
                mNewsInteractor.getNewsDataSource() : mNewsInteractor.getNewsDataSource(mFeedLink);

        mNewsData = new LivePagedListBuilder<>(
                dataSourceFactory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    LiveData<PagedList<News>> getNewsData() {
        return mNewsData;
    }

    LiveData<RequestResult> updateFeed() {
        mRequestResult = mNewsInteractor.updateFeed(mFeedLink);
        return mRequestResult;
    }

    LiveData<RequestResult> getRefreshStatus() {
        return mRequestResult;
    }

    void cancelRefresh() {
        mRequestResult = null;
    }
}
