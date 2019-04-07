package com.object173.newsfeed.features.feed.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.feed.data.LocalDataSource;
import com.object173.newsfeed.features.feed.data.LocalDataSourceImpl;
import com.object173.newsfeed.features.feed.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.feed.domain.FeedInteractor;
import com.object173.newsfeed.features.feed.domain.FeedInteractorImpl;
import com.object173.newsfeed.features.feed.domain.FeedRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FeedViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;
    private final String mFeedLink;

    FeedViewModelFactory(@NonNull Application application, String feedLink) {
        super(application);
        mApplication = application;
        mFeedLink = feedLink;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == FeedViewModel.class) {

            final LocalDataSource dataSource = new LocalDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final FeedRepository repository = new FeedRepositoryImpl(dataSource);
            final FeedInteractor feedInteractor = new FeedInteractorImpl(repository);

            return (T) new FeedViewModel(feedInteractor, mFeedLink);
        }
        return super.create(modelClass);
    }
}
