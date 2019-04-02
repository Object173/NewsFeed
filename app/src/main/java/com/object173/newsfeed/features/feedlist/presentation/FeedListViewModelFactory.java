package com.object173.newsfeed.features.feedlist.presentation;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.feedlist.data.FeedDataSource;
import com.object173.newsfeed.features.feedlist.data.FeedDataSourceImpl;
import com.object173.newsfeed.features.feedlist.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractor;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractorImpl;
import com.object173.newsfeed.features.feedlist.domain.FeedRepository;

public class FeedListViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    FeedListViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == FeedListViewModel.class) {

            final FeedDataSource dataSource = new FeedDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final FeedRepository repository = new FeedRepositoryImpl(dataSource);
            final FeedInteractor interactor = new FeedInteractorImpl(repository);

            return (T) new FeedListViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
