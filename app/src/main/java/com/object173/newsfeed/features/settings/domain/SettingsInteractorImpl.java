package com.object173.newsfeed.features.settings.domain;

import com.object173.newsfeed.features.settings.domain.model.Feed;
import com.object173.newsfeed.features.settings.domain.model.News;

import java.util.Date;
import java.util.List;

public class SettingsInteractorImpl implements SettingsInteractor {

    private final SettingsRepository mRepository;

    public SettingsInteractorImpl(SettingsRepository repository) {
        mRepository = repository;
    }


    @Override
    public List<Feed> getAutoUpdatedFeeds() {
        return mRepository.getAutoUpdatedFeeds();
    }

    @Override
    public void setUpdatedFail(String feedLink) {
        mRepository.setFeedUpdated(feedLink, null);
    }

    @Override
    public void setUpdatedFeed(String feed, Date date) {
        mRepository.setFeedUpdated(feed, date);
    }

    @Override
    public int refreshNews(List<News> newsList, int cacheSize, Date cropDate) {
        return mRepository.refreshNews(newsList, cacheSize, cropDate);
    }
}
