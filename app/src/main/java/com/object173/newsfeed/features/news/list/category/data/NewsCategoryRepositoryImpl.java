package com.object173.newsfeed.features.news.list.category.data;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.google.common.collect.Lists;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.ResponseDTO;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.base.model.pref.CacheConfig;
import com.object173.newsfeed.features.news.list.category.domain.NewsCategoryRepository;
import com.object173.newsfeed.utils.DateUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsCategoryRepositoryImpl implements NewsCategoryRepository {

    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private final LocalNewsDataSource mNewsDataSource;
    private final LocalFeedDataSource mFeedDataSource;
    private final PreferenceDataSource mPreferenceDataSource;
    private final NetworkDataSource mNetworkDataSource;

    public NewsCategoryRepositoryImpl(LocalNewsDataSource newsDataSource, LocalFeedDataSource feedDataSource,
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
    public DataSource.Factory<Integer, News> getNewsByCategory(String category) {
        if(category != null) {
            return mNewsDataSource.getNewsByCategory(category);
        }
        return mNewsDataSource.getNewsDataSource();
    }

    @Override
    public LiveData<Boolean> hideNews(long id) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mExecutorService.execute(() -> result.postValue(mNewsDataSource.hideNews(id)));
        return result;
    }

    @Override
    public void checkReviewed(List<News> reviewedList) {
        mExecutorService.execute(() -> mNewsDataSource.checkReviewed(Lists.transform(reviewedList, News::getId)));
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
                DateUtil.getCropDate(cacheConfig.cacheFrequency));

        return new Pair<>(RequestResult.SUCCESS, count);
    }
}
