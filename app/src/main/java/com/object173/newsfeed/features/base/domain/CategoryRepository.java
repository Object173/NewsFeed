package com.object173.newsfeed.features.base.domain;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.domain.model.local.Category;

import java.util.List;

public interface CategoryRepository {
    LiveData<List<Category>> getCategories();
    DataSource.Factory<Integer, Category> getCategoryDataSource();
    LiveData<Boolean> removeCategory(Category category);
    void addCategory(Category category);
}
