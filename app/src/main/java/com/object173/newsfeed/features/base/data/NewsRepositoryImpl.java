package com.object173.newsfeed.features.base.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.domain.NewsRepository;
import com.object173.newsfeed.features.base.domain.model.local.News;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsRepositoryImpl implements NewsRepository {

    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private final LocalNewsDataSource mNewsDataSource;

    public NewsRepositoryImpl(LocalNewsDataSource newsDataSource) {
        mNewsDataSource = newsDataSource;
    }

    @Override
    public LiveData<News> getNews(Long id) {
        return mNewsDataSource.getNews(id);
    }

    @Override
    public DataSource.Factory<Integer, News> getAllNews() {
        return mNewsDataSource.getNewsDataSource();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByFeed(String feedLink) {
        return mNewsDataSource.getNewsByFeed(feedLink);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByCategory(String category) {
        if(category != null) {
            return mNewsDataSource.getNewsByCategory(category);
        }
        return mNewsDataSource.getNewsDataSource();
    }

    @Override
    public LiveData<Boolean> hideNews(long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mExecutorService.execute(() -> {
            result.postValue(mNewsDataSource.hideNews(id));
        });
        return result;
    }

    @Override
    public void checkReviewed(long id) {
        mExecutorService.execute(() -> {
            mNewsDataSource.checkReviewed(id);
        });
    }
}
