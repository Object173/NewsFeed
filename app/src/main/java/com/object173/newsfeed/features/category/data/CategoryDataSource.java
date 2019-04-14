package com.object173.newsfeed.features.category.data;

import com.object173.newsfeed.features.category.domain.model.Category;

import androidx.paging.DataSource;

public interface CategoryDataSource {
    DataSource.Factory<Integer, Category> getCategoryDataSource();
    void addCategory(Category category);
    void removeCategory(Category category);
}
