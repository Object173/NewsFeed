package com.object173.newsfeed.features.main.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.data.local.LocalCategoryDataSource;
import com.object173.newsfeed.features.base.data.local.LocalCategoryDataSourceImpl;
import com.object173.newsfeed.features.category.data.CategoryRepositoryImpl;
import com.object173.newsfeed.features.category.domain.CategoryRepository;
import com.object173.newsfeed.features.main.domain.CategoryInteractor;
import com.object173.newsfeed.features.main.domain.CategoryInteractorImpl;

public class MainViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    MainViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == MainActivityViewModel.class) {

            final LocalCategoryDataSource dataSource = new LocalCategoryDataSourceImpl(App.getDatabase(mApplication.getApplicationContext()));
            final CategoryRepository repository = new CategoryRepositoryImpl(dataSource);
            final CategoryInteractor interactor = new CategoryInteractorImpl(repository);

            return (T) new MainActivityViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
