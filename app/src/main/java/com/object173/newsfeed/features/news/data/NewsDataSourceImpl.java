package com.object173.newsfeed.features.news.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.DataSource;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.news.domain.model.News;

public class NewsDataSourceImpl implements NewsDataSource {

    private final AppDatabase mDatabase;

    public NewsDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public LiveData<News> getNews(Long id) {
        return Transformations.map(mDatabase.newsDao().get(id), NewsDataSourceImpl::convertToNews);
    }

    private static News convertToNews(NewsDB newsDB) {
        return new News(newsDB.id, newsDB.feedLink, newsDB.title, newsDB.description,
                newsDB.pubDate, newsDB.sourceLink);
    }
}
