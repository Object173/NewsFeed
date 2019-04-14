package com.object173.newsfeed.features.category.presentation;

import com.object173.newsfeed.features.category.domain.CategoryInteractor;
import com.object173.newsfeed.features.category.domain.model.Category;

import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

class CategoryListViewModel extends ViewModel {

    private static final int LOAD_BLOCK_SIZE = 10;
    private static final int PREFETCH_DISTANCE = 5;

    private final CategoryInteractor mInteractor;
    private LiveData<PagedList<Category>> mCategoryData;

    CategoryListViewModel(final CategoryInteractor interactor) {
        mInteractor = interactor;

        final PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(LOAD_BLOCK_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();

        mCategoryData = new LivePagedListBuilder<>(
                interactor.getCategoryDataSource(), config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

    LiveData<PagedList<Category>> getFeedData() {
        return mCategoryData;
    }

    void removeFeed(final int position) {
        mInteractor.removeCategory(mCategoryData.getValue().get(position));
    }

    void addCategory(final Category category) {
        mInteractor.addCategory(category);
    }
}
