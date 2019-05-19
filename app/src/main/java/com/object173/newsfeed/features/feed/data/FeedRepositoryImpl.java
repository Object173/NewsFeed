package com.object173.newsfeed.features.feed.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.ResponseDTO;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.feed.item.domain.FeedItemRepository;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.base.model.pref.CacheConfig;
import com.object173.newsfeed.features.feed.list.domain.FeedListRepository;
import com.object173.newsfeed.utils.DateUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedRepositoryImpl implements FeedItemRepository, FeedListRepository {

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
                    DateUtil.getCropDate(cacheConfig.cacheFrequency));

            result.postValue(RequestResult.SUCCESS);
        });
        return result;
    }

    @Override
    public LiveData<Boolean> removeFeed(String feedLink) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mExecutorService.execute(() -> result.postValue(mFeedDataSource.removeFeed(feedLink)));
        return result;
    }
}
