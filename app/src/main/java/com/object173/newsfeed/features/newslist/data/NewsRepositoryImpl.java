package com.object173.newsfeed.features.newslist.data;

import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.domain.NewsRepository;
import com.object173.newsfeed.features.newslist.domain.model.News;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class NewsRepositoryImpl implements NewsRepository {

    private final LocalDataSource mLocalDataSource;

    public NewsRepositoryImpl(LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public DataSource.Factory<Integer, News> getAllNews() {
        return mLocalDataSource.getNewsDataSource();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByFeed(String feedLink) {
        return mLocalDataSource.getNewsByFeed(feedLink);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByCategory(String category) {
        return mLocalDataSource.getNewsByCategory(category);
    }

    @Override
    public LiveData<Boolean> hideNews(long id) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> {
            result.postValue(mLocalDataSource.hideNews(id) > 0);
        }).start();
        return result;
    }

    @Override
    public void checkReviewed(long id) {
        new Thread(() -> mLocalDataSource.checkReviewed(id)).start();
    }

    @Override
    public void setFeedUpdated(String feedLink, Date updated) {
        mLocalDataSource.setFeedUpdated(feedLink, updated);
    }

    @Override
    public int refreshNews(List<News> newsList, int cacheSize, Date cropDate) {
        List<NewsDB> insertedNews = new LinkedList<>();
        for(News news : newsList) {
            insertedNews.add(convertToNewsDB(news));
        }
        return mLocalDataSource.refreshNews(insertedNews, cacheSize, cropDate);
    }

    @Override
    public List<String> getFeedsById(String category) {
        return mLocalDataSource.getFeedsByCategory(category);
    }

    private static NewsDB convertToNewsDB(final News news) {
        return NewsDB.create(news.getId(), news.getFeedLink(), news.getTitle(), news.getDescription(),
                news.getPubDate(), news.getSourceLink());
    }
}
