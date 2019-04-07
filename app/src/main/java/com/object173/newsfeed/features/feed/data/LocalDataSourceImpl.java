package com.object173.newsfeed.features.feed.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.object173.newsfeed.R;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;

public class LocalDataSourceImpl implements LocalDataSource {

    private final AppDatabase mDatabase;

    public LocalDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public LiveData<Feed> getFeed(String feedLink) {
        return Transformations.map(mDatabase.feedDao().getById(feedLink), LocalDataSourceImpl::convertToFeed);
    }

    @Override
    public boolean isExists(String feedLink) {
        return mDatabase.feedDao().isExist(feedLink) > 0;
    }

    @Override
    public void insertFeed(FeedDB feed) {
        mDatabase.feedDao().insert(feed);
    }

    @Override
    public void insertNews(String feedLink, List<NewsDB> newsList, int cacheSize, Date cropDate) {
        mDatabase.newsDao().insert(newsList);
        mDatabase.newsDao().cropDate(feedLink, cropDate);
        mDatabase.newsDao().cropCount(feedLink, cacheSize);
    }

    @Override
    public LiveData<RequestResult> updateFeed(FeedDB feed) {
        final MutableLiveData<RequestResult> result = new MutableLiveData<>();
        new Thread(() -> {
            result.postValue(RequestResult.RUNNING);
            if(mDatabase.feedDao().updateFeed(feed) > 0) {
                result.postValue(RequestResult.SUCCESS);
            }
            else {
                result.postValue(RequestResult.FAIL);
            }
        }).start();
        return result;
    }

    @Override
    public int getCacheSize(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_key_cache_size),
                context.getResources().getInteger(R.integer.cache_size_default));
    }

    @Override
    public int getCacheFrequency(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                context.getResources().getInteger(R.integer.cache_frequency_default));
    }

    private static Feed convertToFeed(FeedDB feedDB) {
        return new Feed(feedDB.link, feedDB.title, feedDB.description, feedDB.sourceLink,
                feedDB.updated, feedDB.iconLink, feedDB.author, feedDB.customName,
                feedDB.isAutoRefresh, feedDB.isMainChannel);
    }
}
