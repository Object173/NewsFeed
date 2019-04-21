package com.object173.newsfeed.features.newslist.category.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.NewsRepository;
import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;

public class NewsCategoryInteractorImpl implements NewsCategoryInteractor {

    private final NewsRepository mRepository;
    private final FeedRepository mFeedRepository;

    public NewsCategoryInteractorImpl(NewsRepository repository, FeedRepository feedRepository) {
        mRepository = repository;
        mFeedRepository = feedRepository;
    }

    @Override
    public DataSource.Factory<Integer, News> getAllNews() {
        return mRepository.getAllNews();
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
    public LiveData<RequestResult> updateCategory(String category) {
        return mFeedRepository.updateCategoryAsync(category);
    }
}
