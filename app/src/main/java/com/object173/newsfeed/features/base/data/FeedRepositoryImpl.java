package com.object173.newsfeed.features.base.data;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.ResponseDTO;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.model.local.Feed;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;
import com.object173.newsfeed.features.base.domain.model.pref.CacheConfig;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedRepositoryImpl implements FeedRepository {

    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private final LocalFeedDataSource mFeedDataSource;
    private final LocalNewsDataSource mNewsDataSource;
    private final PreferenceDataSource mPreferenceDataSource;
    private final NetworkDataSource mNetworkDataSource;

    public FeedRepositoryImpl(LocalFeedDataSource feedDataSource, LocalNewsDataSource newsDataSource,
                              PreferenceDataSource preferenceDataSource, NetworkDataSource networkDataSource) {
        mFeedDataSource = feedDataSource;
        mNewsDataSource = newsDataSource;
        mPreferenceDataSource = preferenceDataSource;
        mNetworkDataSource = networkDataSource;
    }

    @Override
    public LiveData<Feed> getFeed(String feedLink) {
        return mFeedDataSource.getFeedAsync(feedLink);
    }

    @Override
    public DataSource.Factory<Integer, Feed> getLocalDataSource() {
        return mFeedDataSource.getFeedDataSource();
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource(String category) {
        if(category != null) {
            return mFeedDataSource.getFeedDataSource(category);
        }
        return mFeedDataSource.getFeedDataSource();
    }

    @Override
    public List<String> getAutoUpdateList() {
        return mFeedDataSource.getAutoUpdatedList();
    }

    @Override
    public LiveData<Boolean> updateFeed(Feed feed) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mExecutorService.execute(() -> result.postValue(mFeedDataSource.updateFeed(feed)));
        return result;
    }

    @Override
    public LiveData<RequestResult> insertFeed(Feed feed) {
        MutableLiveData<RequestResult> result = new MutableLiveData<>();
        mExecutorService.execute(() -> {
            result.postValue(RequestResult.RUNNING);

            if(mFeedDataSource.isExist(feed.getLink())) {
                result.postValue(RequestResult.EXISTS);
                return;
            }

            ResponseDTO response = mNetworkDataSource.getNewsFeed(feed.getLink());
            if(response.result != RequestResult.SUCCESS) {
                result.postValue(response.result);
                return;
            }

            Feed newFeed = response.feed;
            newFeed.setAutoRefresh(feed.isAutoRefresh());
            newFeed.setCategory(feed.getCategory());
            newFeed.setIsCustomName(feed.getIsCustomName());
            newFeed.setCustomName(feed.getCustomName());

            mFeedDataSource.insertFeed(newFeed);

            CacheConfig cacheConfig = mPreferenceDataSource.getCacheConfig();
            mNewsDataSource.insertNews(response.newsList, cacheConfig.cacheSize,
                    getCropDate(cacheConfig.cacheFrequency));

            result.postValue(RequestResult.SUCCESS);
        });
        return result;
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

    @Override
    public LiveData<RequestResult> updateCategoryAsync(String category) {
        MutableLiveData<RequestResult> result = new MutableLiveData<>();
        mExecutorService.execute(() -> {
            result.postValue(RequestResult.RUNNING);

            List<String> feedList = mFeedDataSource.getFeedsByCategory(category);
            if(feedList.isEmpty()) {
                result.postValue(RequestResult.SUCCESS);
                return;
            }

            RequestResult requestResult = null;
            for(String feedLink : feedList) {
                RequestResult currentResult = updateFeedNews(feedLink).first;
                if(requestResult != RequestResult.SUCCESS) {
                    requestResult = currentResult;
                }
            }

            result.postValue(requestResult);
        });
        return result;
    }

    @Override
    public Pair<RequestResult, Integer> updateFeedNews(String feedLink) {
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

    @Override
    public LiveData<Boolean> removeFeed(String feedLink) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mExecutorService.execute(() -> result.postValue(mFeedDataSource.removeFeed(feedLink)));
        return result;
    }

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }
}
