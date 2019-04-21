package com.object173.newsfeed.features.news.list.feed.data;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.ResponseDTO;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.base.model.pref.CacheConfig;
import com.object173.newsfeed.features.news.list.feed.domain.NewsFeedRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsFeedRepositoryImpl implements NewsFeedRepository {

    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private final LocalNewsDataSource mNewsDataSource;
    private final LocalFeedDataSource mFeedDataSource;
    private final PreferenceDataSource mPreferenceDataSource;
    private final NetworkDataSource mNetworkDataSource;

    public NewsFeedRepositoryImpl(LocalNewsDataSource newsDataSource, LocalFeedDataSource feedDataSource,
                                  PreferenceDataSource preferenceDataSource, NetworkDataSource networkDataSource) {
        mNewsDataSource = newsDataSource;
        mFeedDataSource = feedDataSource;
        mPreferenceDataSource = preferenceDataSource;
        mNetworkDataSource = networkDataSource;
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

    @Override
    public LiveData<RequestResult> updateFeedNewsAsync(String feedLink) {
        MutableLiveData<RequestResult> result = new MutableLiveData<>();
        mExecutorService.execute(() -> {
            result.postValue(RequestResult.RUNNING);
            result.postValue(updateFeedNews(feedLink).first);
        });
        return result;
    }

    private Pair<RequestResult, Integer> updateFeedNews(String feedLink) {
        Feed feed = mFeedDataSource.getFeed(feedLink);
        if(feed == null) {
            return new Pair<>(RequestResult.NOT_EXIST, 0);
        }

        ResponseDTO response = mNetworkDataSource.getNewsFeed(feedLink);
        if(response.result != RequestResult.SUCCESS) {
            return new Pair<>(response.result, 0);
        }

        Feed newFeed = response.feed;
        newFeed.setAutoRefresh(feed.isAutoRefresh());
        newFeed.setCategory(feed.getCategory());
        newFeed.setIsCustomName(feed.getIsCustomName());
        newFeed.setCustomName(feed.getCustomName());

        mFeedDataSource.updateFeed(newFeed);

        CacheConfig cacheConfig = mPreferenceDataSource.getCacheConfig();
        int count = mNewsDataSource.insertNews(response.newsList, cacheConfig.cacheSize,
                getCropDate(cacheConfig.cacheFrequency));

        return new Pair<>(RequestResult.SUCCESS, count);
    }

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }
}
