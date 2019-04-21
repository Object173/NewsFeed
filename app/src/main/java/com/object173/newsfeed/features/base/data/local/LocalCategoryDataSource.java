package com.object173.newsfeed.features.base.data.local;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.model.local.Category;

public interface LocalCategoryDataSource {
    LiveData<List<Category>> getCategories();
    DataSource.Factory<Integer, Category> getCategoryDataSource();
    void addCategory(Category category);
    boolean removeCategory(Category category);
}
