package com.object173.newsfeed.features.category.presentation;

import com.object173.newsfeed.features.base.domain.model.local.Category;
import com.object173.newsfeed.features.base.presentation.BaseListFragmentViewModel;
import com.object173.newsfeed.features.category.domain.CategoryInteractor;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

public class CategoryListViewModel extends BaseListFragmentViewModel<Category> {

    private final CategoryInteractor mInteractor;

    CategoryListViewModel(final CategoryInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    protected DataSource.Factory<Integer, Category> loadFactoryData(String param) {
        return mInteractor.getCategoryDataSource();
    }

    @Override
    protected DataSource.Factory<Integer, Category> loadFactoryData() {
        return mInteractor.getCategoryDataSource();
    }

    @Override
    protected LiveData<Boolean> removeData(Category category) {
        return mInteractor.removeCategory(category);
    }

    @Override
    public void addData(Category obj) {
        mInteractor.addCategory(obj);
    }
}
