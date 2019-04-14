package com.object173.newsfeed.features.category.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.category.data.CategoryDataSource;
import com.object173.newsfeed.features.category.data.CategoryDataSourceImpl;
import com.object173.newsfeed.features.category.data.CategoryRepositoryImpl;
import com.object173.newsfeed.features.category.domain.CategoryInteractor;
import com.object173.newsfeed.features.category.domain.CategoryInteractorImpl;
import com.object173.newsfeed.features.category.domain.CategoryRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CategoryListViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    CategoryListViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CategoryListViewModel.class) {

            final CategoryDataSource dataSource = new CategoryDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final CategoryRepository repository = new CategoryRepositoryImpl(dataSource);
            final CategoryInteractor interactor = new CategoryInteractorImpl(repository);

            return (T) new CategoryListViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
