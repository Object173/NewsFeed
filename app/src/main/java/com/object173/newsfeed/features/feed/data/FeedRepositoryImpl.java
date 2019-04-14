package com.object173.newsfeed.features.feed.data;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.feed.domain.FeedRepository;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.features.feed.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class FeedRepositoryImpl implements FeedRepository {

    private final LocalDataSource mLocalDataSource;

    public FeedRepositoryImpl(LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public LiveData<Feed> getFeed(final String feedLink) {
        return mLocalDataSource.getFeed(feedLink);
    }

    @Override
    public LiveData<RequestResult> updateFeed(Feed feed) {
        return mLocalDataSource.updateFeed(feed);
    }

    @Override
    public boolean insertFeed(Feed feed) {
        return mLocalDataSource.insertFeed(feed);
    }

    @Override
    public void insertNews(List<News> newsList, int cacheSize, Date cropDate) {
        mLocalDataSource.insertNews(newsList, cacheSize, cropDate);
    }
}
