package com.object173.newsfeed.features.category.data;

import com.object173.newsfeed.features.category.domain.CategoryRepository;
import com.object173.newsfeed.features.category.domain.model.Category;

import androidx.paging.DataSource;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryDataSource mCategoryDataSource;

    public CategoryRepositoryImpl(CategoryDataSource categoryDataSource) {
        mCategoryDataSource = categoryDataSource;
    }

    @Override
    public DataSource.Factory<Integer, Category> getCategoryDataSource() {
        return mCategoryDataSource.getCategoryDataSource();
    }

    @Override
    public void removeFeed(Category category) {
        new Thread(() -> mCategoryDataSource.removeCategory(category)).start();
    }

    @Override
    public void addCategory(Category category) {
        new Thread(() -> mCategoryDataSource.addCategory(category)).start();
    }
}
