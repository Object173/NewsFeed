package com.object173.newsfeed.features.feed.item.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSourceImpl;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSourceImpl;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.feed.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.feed.item.domain.FeedInteractor;
import com.object173.newsfeed.features.feed.item.domain.FeedInteractorImpl;
import com.object173.newsfeed.features.feed.item.domain.FeedItemRepository;

public class FeedViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    FeedViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == FeedViewModel.class) {

            final AppDatabase database = App.getDatabase(mApplication.getApplicationContext());

            final LocalNewsDataSource mNewsDataSource = new LocalNewsDataSourceImpl(database);
            final LocalFeedDataSource mFeedDataSource = new LocalFeedDataSourceImpl(database);
            final PreferenceDataSource mPreferenceDataSource = App.getPreferenceDataSource(mApplication.getApplicationContext());
            final NetworkDataSource mNetworkDataSource = App.getNetworkDataSource(mApplication.getApplicationContext());

            final FeedItemRepository repository = new FeedRepositoryImpl(mFeedDataSource, mNewsDataSource,
                    mPreferenceDataSource, mNetworkDataSource);
            final FeedInteractor interactor = new FeedInteractorImpl(repository);

            return (T) new FeedViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
