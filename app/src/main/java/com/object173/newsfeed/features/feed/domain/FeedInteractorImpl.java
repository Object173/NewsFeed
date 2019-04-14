package com.object173.newsfeed.features.feed.domain;

import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.features.feed.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;

public class FeedInteractorImpl implements FeedInteractor {

    private final FeedRepository mFeedRepository;

    public FeedInteractorImpl(FeedRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public LiveData<Feed> getFeed(String feedLink) {
        return mFeedRepository.getFeed(feedLink);
    }

    @Override
    public LiveData<RequestResult> updateFeed(Feed feed) {
        return mFeedRepository.updateFeed(feed);
    }

    @Override
    public boolean insertFeed(Feed feed) {
        return mFeedRepository.insertFeed(feed);
    }

    @Override
    public void insertNews(List<News> newsList, int cacheSize, Date cropDate) {
        mFeedRepository.insertNews(newsList, cacheSize, cropDate);
    }
}
