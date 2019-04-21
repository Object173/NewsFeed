package com.object173.newsfeed.features.category.domain;

import com.object173.newsfeed.features.base.domain.model.local.Category;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public interface CategoryInteractor {
    DataSource.Factory<Integer, Category> getCategoryDataSource();
    void addCategory(Category category);
    LiveData<Boolean> removeCategory(Category category);
}
