package com.object173.newsfeed.features.settings.data;

import android.util.Pair;

import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.ResponseDTO;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.base.model.pref.CacheConfig;
import com.object173.newsfeed.features.settings.domain.UpdateRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateRepositoreImpl implements UpdateRepository {

    private final LocalNewsDataSource mNewsDataSource;
    private final LocalFeedDataSource mFeedDataSource;
    private final NetworkDataSource mNetworkDataSource;
    private final PreferenceDataSource mPreferenceDataSource;

    public UpdateRepositoreImpl(LocalNewsDataSource newsDataSource, LocalFeedDataSource feedDataSource,
                                NetworkDataSource networkDataSource, PreferenceDataSource preferenceDataSource) {
        mNewsDataSource = newsDataSource;
        mFeedDataSource = feedDataSource;
        mNetworkDataSource = networkDataSource;
        mPreferenceDataSource = preferenceDataSource;
    }

    @Override
    public List<String> getAutoUpdateList() {
        return mFeedDataSource.getAutoUpdatedList();
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

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }
}
