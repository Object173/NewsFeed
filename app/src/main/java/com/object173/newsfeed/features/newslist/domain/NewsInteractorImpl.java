package com.object173.newsfeed.features.newslist.domain;

import com.object173.newsfeed.features.newslist.domain.model.News;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class NewsInteractorImpl implements NewsInteractor {

    private final NewsRepository mRepository;

    public NewsInteractorImpl(NewsRepository newsRepository) {
        mRepository = newsRepository;
    }

    @Override
    public DataSource.Factory<Integer, News> getAllNews() {
        return mRepository.getAllNews();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByFeed(String feedLink) {
        return mRepository.getNewsByFeed(feedLink);
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsByCategory(String category) {
        return mRepository.getNewsByCategory(category);
    }

    @Override
    public LiveData<Boolean> hideNews(long id) {
        return mRepository.hideNews(id);
    }

    @Override
    public void checkReviewed(long id) {
        mRepository.checkReviewed(id);
    }

    @Override
    public void setFeedUpdated(String feed, Date date) {
        mRepository.setFeedUpdated(feed, date);
    }

    @Override
    public int refreshNews(List<News> newsList, int cacheSize, Date cropDate) {
        return mRepository.refreshNews(newsList, cacheSize, cropDate);
    }

    @Override
    public List<String> getFeedsByCategory(String category) {
        return mRepository.getFeedsById(category);
    }
}
