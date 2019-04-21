package com.object173.newsfeed.features.base.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.CategoryDB;
import com.object173.newsfeed.features.base.model.local.Category;

import java.util.LinkedList;
import java.util.List;

public class LocalCategoryDataSourceImpl implements LocalCategoryDataSource {

    private final AppDatabase mDatabase;

    public LocalCategoryDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return Transformations.map(mDatabase.categoryDao().getCategories(), input -> {
            List<Category> result = new LinkedList<>();
            for(CategoryDB categoryDB : input) {
                result.add(convertToCategory(categoryDB));
            }
            return result;
        });
    }

    @Override
    public DataSource.Factory<Integer, Category> getCategoryDataSource() {
        return mDatabase.categoryDao().getAll().map(LocalCategoryDataSourceImpl::convertToCategory);
    }

    @Override
    public void addCategory(Category category) {
        mDatabase.categoryDao().add(convertToCategoryDB(category));
    }

    @Override
    public boolean removeCategory(Category category) {
        return mDatabase.categoryDao().remove(convertToCategoryDB(category)) > 0;
    }

    private static CategoryDB convertToCategoryDB(Category category) {
        return CategoryDB.create(category.getTitle());
    }

    private static Category convertToCategory(CategoryDB categoryDB) {
        return new Category(categoryDB.title);
    }
}
