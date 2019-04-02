package com.object173.newsfeed.features.newslist.data;

import android.arch.paging.DataSource;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.domain.model.News;

public class NewsDataSourceImpl implements NewsDataSource {

    private final AppDatabase mDatabase;

    public NewsDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource() {
        return mDatabase.newsDao().getAllDataSource().map(NewsDataSourceImpl::convertToNews);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource(final String feedLink) {
        return mDatabase.newsDao().getAllDataSource(feedLink).map(NewsDataSourceImpl::convertToNews);
    }

    private static News convertToNews(NewsDB newsDB) {
        return new News(newsDB.id, newsDB.feedLink, newsDB.title, newsDB.description,
                newsDB.pubDate, newsDB.sourceLink);
    }
}
