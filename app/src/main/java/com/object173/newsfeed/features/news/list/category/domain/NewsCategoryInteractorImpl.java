package com.object173.newsfeed.features.news.list.category.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;

public class NewsCategoryInteractorImpl implements NewsCategoryInteractor {

    private final NewsCategoryRepository mRepository;

    public NewsCategoryInteractorImpl(NewsCategoryRepository repository) {
        mRepository = repository;
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
        return mRepository.updateCategoryAsync(category);
    }
}
