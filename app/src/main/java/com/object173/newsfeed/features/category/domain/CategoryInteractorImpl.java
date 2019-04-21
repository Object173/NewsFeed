package com.object173.newsfeed.features.category.domain;

import com.object173.newsfeed.features.base.domain.CategoryRepository;
import com.object173.newsfeed.features.base.domain.model.local.Category;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class CategoryInteractorImpl implements CategoryInteractor {

    private final CategoryRepository mCategoryRepository;

    public CategoryInteractorImpl(CategoryRepository categoryRepository) {
        mCategoryRepository = categoryRepository;
    }

    @Override
    public DataSource.Factory<Integer, Category> getCategoryDataSource() {
        return mCategoryRepository.getCategoryDataSource();
    }

    @Override
    public void addCategory(Category category) {
        mCategoryRepository.addCategory(category);
    }

    @Override
    public LiveData<Boolean> removeCategory(Category category) {
        return mCategoryRepository.removeCategory(category);
    }
}