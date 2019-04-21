package com.object173.newsfeed.features.category.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.object173.newsfeed.features.base.data.local.LocalCategoryDataSource;
import com.object173.newsfeed.features.category.domain.CategoryRepository;
import com.object173.newsfeed.features.base.model.local.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final LocalCategoryDataSource mDataSource;
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public CategoryRepositoryImpl(LocalCategoryDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return mDataSource.getCategories();
    }

    @Override
    public DataSource.Factory<Integer, Category> getCategoryDataSource() {
        return mDataSource.getCategoryDataSource();
    }

    @Override
    public LiveData<Boolean> removeCategory(Category category) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        mExecutorService.execute(() -> {
            result.postValue(mDataSource.removeCategory(category));
        });
        return result;
    }

    @Override
    public void addCategory(Category category) {
        mExecutorService.execute(() -> mDataSource.addCategory(category));
    }
}
