package com.object173.newsfeed.features.feed.data;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.features.feed.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

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
    public boolean insertFeed(Feed feed) {
        if(mDatabase.feedDao().isExist(feed.getLink()) > 0) {
            return false;
        }
        return mDatabase.feedDao().insert(convertToFeedDB(feed)) > 0;
    }

    @Override
    public void insertNews(List<News> newsList, int cacheSize, Date cropDate) {
        for(News news : newsList) {
            if(mDatabase.newsDao().isExist(news.getFeedLink(), news.getPubDate()) > 0) {
                continue;
            }
            mDatabase.newsDao().insert(convertToNewsDB(news));
        }

        if(!newsList.isEmpty()) {
            mDatabase.newsDao().cropDate(newsList.get(0).getFeedLink(), cropDate);
            mDatabase.newsDao().cropCount(newsList.get(0).getFeedLink(), cacheSize);
        }
    }

    @Override
    public LiveData<RequestResult> updateFeed(Feed feed) {
        final MutableLiveData<RequestResult> result = new MutableLiveData<>();
        new Thread(() -> {
            result.postValue(RequestResult.RUNNING);
            FeedDB feedDB = convertToFeedDB(feed);
            if(mDatabase.feedDao().updateFeed(feedDB) > 0) {
                result.postValue(RequestResult.SUCCESS);
            }
            else {
                result.postValue(RequestResult.FAIL);
            }
        }).start();
        return result;
    }

    private static Feed convertToFeed(FeedDB feedDB) {
        return new Feed(feedDB.link, feedDB.title, feedDB.description, feedDB.sourceLink,
                feedDB.updated, feedDB.iconLink, feedDB.author, feedDB.customName,
                feedDB.isAutoRefresh, feedDB.category);
    }

    private static FeedDB convertToFeedDB(Feed feed) {
        return FeedDB.create(feed.getLink(), feed.getTitle(), feed.getDescription(), feed.getSourceLink(),
                feed.getUpdated(), feed.getIconLink(), feed.getAuthor(), feed.getCustomName(),
                feed.isAutoRefresh(), feed.getCategory());
    }

    private static NewsDB convertToNewsDB(final News news) {
        return NewsDB.create(news.getId(), news.getFeedLink(), news.getTitle(), news.getDescription(),
                news.getPubDate(), news.getSourceLink());
    }
}
