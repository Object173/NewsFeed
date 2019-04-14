package com.object173.newsfeed.features.category.data;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.CategoryDB;
import com.object173.newsfeed.features.category.domain.model.Category;

import androidx.paging.DataSource;

public class CategoryDataSourceImpl implements CategoryDataSource {

    private final AppDatabase mDatabase;

    public CategoryDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public DataSource.Factory<Integer, Category> getCategoryDataSource() {
        return mDatabase.categoryDao().getAll().map(CategoryDataSourceImpl::convertToCategory);
    }

    @Override
    public void addCategory(Category category) {
        if(category.getTitle().length() == 0) {
            return;
        }
        new Thread(() -> {
            if(mDatabase.categoryDao().getCategory(category.getTitle()) != null) {
                return;
            }
            mDatabase.categoryDao().add(convertToCategoryDB(category));
        }).start();
    }

    @Override
    public void removeCategory(Category category) {
        new Thread(() -> {
            mDatabase.categoryDao().remove(convertToCategoryDB(category));
        }).start();
    }

    private static CategoryDB convertToCategoryDB(Category category) {
        return CategoryDB.create(category.getTitle());
    }

    private static Category convertToCategory(CategoryDB categoryDB) {
        return new Category(categoryDB.title);
    }
}
