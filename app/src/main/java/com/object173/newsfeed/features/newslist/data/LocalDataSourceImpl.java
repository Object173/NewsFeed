package com.object173.newsfeed.features.newslist.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.object173.newsfeed.R;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.paging.DataSource;
import androidx.preference.PreferenceManager;

public class LocalDataSourceImpl implements LocalDataSource {

    private final AppDatabase mDatabase;

    public LocalDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource() {
        return mDatabase.newsDao().getAllDataSource().map(LocalDataSourceImpl::convertToNews);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource(final String feedLink) {
        return mDatabase.newsDao().getAllDataSource(feedLink).map(LocalDataSourceImpl::convertToNews);
    }

    @Override
    public boolean isExist(String feedLink, Date pubDate) {
        return mDatabase.newsDao().isExist(feedLink, pubDate) > 0;
    }

    @Override
    public int hideNews(long id) {
        return mDatabase.newsDao().setHidden(id, true);
    }

    @Override
    public void checkReviewed(long id) {
        mDatabase.newsDao().setReviewed(id, true);
    }

    @Override
    public boolean isFeedExists(String feedLink) {
        return mDatabase.feedDao().isExist(feedLink) > 0;
    }

    @Override
    public void setFeedUpdated(String feedLink, Date updated) {
        mDatabase.feedDao().setUpdated(feedLink, updated);
    }

    @Override
    public long refreshNews(String feedLink, List<NewsDB> newsList, int cacheSize, Date cropDate) {
        long count = mDatabase.newsDao().insert(newsList).length;
        mDatabase.newsDao().cropDate(feedLink, cropDate);
        mDatabase.newsDao().cropCount(feedLink, cacheSize);

        return count;
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

    private static News convertToNews(NewsDB newsDB) {
        return new News(newsDB.id, newsDB.title, newsDB.pubDate, newsDB.isReviewed);
    }
}
