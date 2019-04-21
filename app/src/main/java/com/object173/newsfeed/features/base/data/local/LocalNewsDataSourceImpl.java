package com.object173.newsfeed.features.base.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.base.domain.model.local.News;

import java.util.Date;
import java.util.List;

public class LocalNewsDataSourceImpl implements LocalNewsDataSource {

    private final AppDatabase mDatabase;

    public LocalNewsDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public LiveData<News> getNews(Long id) {
        return Transformations.map(mDatabase.newsDao().get(id), LocalNewsDataSourceImpl::convertToNews);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource() {
        return mDatabase.newsDao().getAllDataSource().map(LocalNewsDataSourceImpl::convertToNews);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByFeed(String feedLink) {
        return mDatabase.newsDao().getAllDataSource(feedLink).map(LocalNewsDataSourceImpl::convertToNews);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByCategory(String category) {
        return mDatabase.newsDao().getByCategory(category).map(LocalNewsDataSourceImpl::convertToNews);
    }

    @Override
    public int insertNews(List<News> newsList, int cacheSize, Date cropDate) {
        if(newsList.isEmpty()) {
            return 0;
        }

        String feedLink = newsList.get(0).getFeedLink();
        int count = 0;

        for(int i = 0; i < newsList.size() && i < cacheSize; i++) {
            NewsDB newsDB = convertToNewsDB(newsList.get(i));
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
    public boolean isNewsExist(String feedLink, Date pubDate) {
        return mDatabase.newsDao().isExist(feedLink, pubDate) > 0;
    }

    @Override
    public boolean hideNews(long id) {
        return mDatabase.newsDao().setHidden(id, true) > 0;
    }

    @Override
    public void checkReviewed(long id) {
        mDatabase.newsDao().setReviewed(id, true);
    }

    private static News convertToNews(NewsDB newsDB) {
        return new News(newsDB.id, newsDB.feedLink, newsDB.title, newsDB.description,
                newsDB.pubDate, newsDB.sourceLink);
    }

    private static NewsDB convertToNewsDB(final News news) {
        return NewsDB.create(news.getId(), news.getFeedLink(), news.getTitle(), news.getDescription(),
                news.getPubDate(), news.getSourceLink());
    }
}
