package com.object173.newsfeed.features.newslist.presentation;

import com.object173.newsfeed.features.newslist.device.UpdateFeedWorker;
import com.object173.newsfeed.features.newslist.domain.NewsInteractor;
import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

class NewsListViewModel extends ViewModel {

    private static final int LOAD_BLOCK_SIZE = 10;
    private static final int PREFETCH_DISTANCE = 10;

    private final NewsInteractor mNewsInteractor;

    private final String mFeedLink;
    private String mCategory;

    private LiveData<PagedList<News>> mNewsData;
    private LiveData<RequestResult> mRequestResult;

    private final List<Long> mReviewedNews = new LinkedList<>();

    NewsListViewModel(final NewsInteractor newsInteractor, final String feedLink, final String category) {
        mNewsInteractor = newsInteractor;
        mFeedLink = feedLink;
        mCategory = category;

        if(mFeedLink != null) {
            mNewsData = loadNews(mNewsInteractor.getNewsByFeed(mFeedLink));
        }
        else {
            if(mCategory != null) {
                mNewsData = loadNews(mNewsInteractor.getNewsByCategory(mCategory));
            }
            else {
                mNewsData = loadNews(mNewsInteractor.getAllNews());
            }
        }
    }

    private LiveData<PagedList<News>> loadNews (DataSource.Factory<Integer, News> factory) {
        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(LOAD_BLOCK_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        return new LivePagedListBuilder<>(
                factory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    LiveData<PagedList<News>> setCategory(String category) {
        mCategory = category;
        if(mCategory != null) {
            return loadNews(mNewsInteractor.getNewsByCategory(mCategory));
        }
        else {
            return loadNews(mNewsInteractor.getAllNews());
        }
    }

    LiveData<PagedList<News>> getNewsData() {
        return mNewsData;
    }

    LiveData<RequestResult> updateFeed() {
        if(mFeedLink != null) {
            mRequestResult = UpdateFeedWorker.startByFeed(mFeedLink);
        }
        else {
            mRequestResult = UpdateFeedWorker.startByCategory(mCategory);
        }
        return mRequestResult;
    }

    LiveData<RequestResult> getRefreshStatus() {
        return mRequestResult;
    }

    void cancelRefresh() {
        mRequestResult = null;
    }

    LiveData<Boolean> hideNews(int position) {
        return mNewsInteractor.hideNews(mNewsData.getValue().get(position).getId());
    }

    void onNewsReviewed(final News news) {
        mReviewedNews.add(news.getId());
    }

    void setReviewedNews() {
        for(Long id : mReviewedNews) {
            mNewsInteractor.checkReviewed(id);
        }
        mReviewedNews.clear();
    }
}
