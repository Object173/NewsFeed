package com.object173.newsfeed.features.settings.domain;

import com.object173.newsfeed.features.settings.domain.model.Feed;
import com.object173.newsfeed.features.settings.domain.model.News;

import java.util.Date;
import java.util.List;

public interface SettingsRepository {
    List<Feed> getAutoUpdatedFeeds();

    void setFeedUpdated(String feedLink, Date updated);
    int refreshNews(List<News> newsList, int cacheSize, Date cropDate);
}
