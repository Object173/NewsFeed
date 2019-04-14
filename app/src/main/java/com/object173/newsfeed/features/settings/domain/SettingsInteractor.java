package com.object173.newsfeed.features.settings.domain;

import com.object173.newsfeed.features.settings.domain.model.Feed;
import com.object173.newsfeed.features.settings.domain.model.News;

import java.util.Date;
import java.util.List;

public interface SettingsInteractor {
    List<Feed> getAutoUpdatedFeeds();

    void setUpdatedFail(String feedLink);
    void setUpdatedFeed(String feed, Date date);
    int refreshNews(List<News> newsList, int cacheSize, Date cropDate);
}
