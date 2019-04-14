package com.object173.newsfeed.features.category.domain;

import com.object173.newsfeed.features.category.domain.model.Category;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface CategoryRepository {
    DataSource.Factory<Integer, Category> getCategoryDataSource();
    void removeFeed(Category category);
    void addCategory(Category category);
}
