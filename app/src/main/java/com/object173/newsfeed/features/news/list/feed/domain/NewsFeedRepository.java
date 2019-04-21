package com.object173.newsfeed.features.news.list.feed.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

public interface NewsFeedRepository {
    LiveData<News> getNews(Long id);

    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByFeed(String feedLink);

    LiveData<RequestResult> updateFeedNewsAsync(String feedLink);

    LiveData<Boolean> hideNews(long id);
    void checkReviewed(long id);
}
