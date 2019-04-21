package com.object173.newsfeed.features.feedlist.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractor;
import com.object173.newsfeed.features.feedlist.domain.FeedInteractorImpl;

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

            final FeedRepository repository = App.getFeedRepository(mApplication.getApplicationContext());
            final FeedInteractor interactor = new FeedInteractorImpl(repository);

            return (T) new FeedListFragmentViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
