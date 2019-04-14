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
    public DataSource.Factory<Integer, News> getNewsByFeed(final String feedLink) {
        return mDatabase.newsDao().getAllDataSource(feedLink).map(LocalDataSourceImpl::convertToNews);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByCategory(String category) {
        return mDatabase.newsDao().getByCategory(category).map(LocalDataSourceImpl::convertToNews);
    }

    @Override
    public boolean isNewsExist(String feedLink, Date pubDate) {
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
    public void setFeedUpdated(String feedLink, Date updated) {
        mDatabase.feedDao().setUpdated(feedLink, updated);
    }

    @Override
    public int refreshNews(List<NewsDB> newsList, int cacheSize, Date cropDate) {
        if(newsList.isEmpty()) {
            return 0;
        }

        String feedLink = newsList.get(0).feedLink;
        int count = 0;

        for(int i = 0; i < newsList.size() && i < cacheSize; i++) {
            NewsDB newsDB = newsList.get(i);
            if(isNewsExist(feedLink, newsDB.pubDate) ||
                    newsDB.pubDate.getTime() <= cropDate.getTime()) {
                break;
            }
            mDatabase.newsDao().insert(newsDB);
            count++;
        }

        mDatabase.newsDao().cropDate(feedLink, cropDate);
        mDatabase.newsDao().cropCount(feedLink, cacheSize);

        return count;
    }

    @Override
    public List<String> getFeedsByCategory(String category) {
        if(category != null) {
            return mDatabase.feedDao().getListByCategory(category);
        }
        else {
            return mDatabase.feedDao().getListByCategory();
        }
    }

    private static News convertToNews(NewsDB newsDB) {
        return new News(newsDB.id, newsDB.feedLink, newsDB.title, newsDB.description,
                newsDB.pubDate, newsDB.sourceLink);
    }
}
