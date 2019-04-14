package com.object173.newsfeed.features.feedlist.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.feedlist.data.LocalDataSource;
import com.object173.newsfeed.features.feedlist.data.LocalDataSourceImpl;
import com.object173.newsfeed.features.feedlist.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.feedlist.domain.CategoryInteractor;
import com.object173.newsfeed.features.feedlist.domain.CategoryInteractorImpl;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractor;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractorImpl;
import com.object173.newsfeed.features.feedlist.domain.FeedRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FeedListViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    FeedListViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == FeedListFragmentViewModel.class) {
            final LocalDataSource dataSource = new LocalDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final FeedRepository repository = new FeedRepositoryImpl(dataSource);
            final FeedInteractor interactor = new FeedInteractorImpl(repository);

            return (T) new FeedListFragmentViewModel(interactor);
        }
        if(modelClass == FeedListActivityViewModel.class) {
            final LocalDataSource dataSource = new LocalDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final FeedRepository repository = new FeedRepositoryImpl(dataSource);
            final CategoryInteractor interactor = new CategoryInteractorImpl(repository);

            return (T) new FeedListActivityViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
