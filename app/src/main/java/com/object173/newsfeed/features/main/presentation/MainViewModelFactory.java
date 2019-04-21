package com.object173.newsfeed.features.main.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.domain.CategoryRepository;
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

            final CategoryRepository repository = App.getCategoryRepository(mApplication.getApplicationContext());
            final CategoryInteractor interactor = new CategoryInteractorImpl(repository);

            return (T) new MainActivityViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
