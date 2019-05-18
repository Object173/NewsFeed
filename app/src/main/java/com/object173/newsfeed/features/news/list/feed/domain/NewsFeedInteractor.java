package com.object173.newsfeed.features.news.list.feed.domain;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import java.util.List;

public interface NewsFeedInteractor {
    DataSource.Factory<Integer, News> getAllNews();
    DataSource.Factory<Integer, News> getNewsByFeed(String feedLink);

    LiveData<Boolean> hideNews(long id);
    void checkReviewed(List<News> reviewedList);

    LiveData<RequestResult> updateFeedNews(String feedLink);
}
