package com.object173.newsfeed.features.feed.list.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSourceImpl;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSourceImpl;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.feed.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.feed.item.domain.FeedItemRepository;
import com.object173.newsfeed.features.feed.list.domain.FeedInteractor;
import com.object173.newsfeed.features.feed.list.domain.FeedInteractorImpl;
import com.object173.newsfeed.features.feed.list.domain.FeedListRepository;
import com.object173.newsfeed.features.feed.list.presentation.FeedListFragmentViewModel;

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

            final AppDatabase database = App.getDatabase(mApplication.getApplicationContext());

            final LocalNewsDataSource mNewsDataSource = new LocalNewsDataSourceImpl(database);
            final LocalFeedDataSource mFeedDataSource = new LocalFeedDataSourceImpl(database);
            final PreferenceDataSource mPreferenceDataSource = App.getPreferenceDataSource(mApplication.getApplicationContext());
            final NetworkDataSource mNetworkDataSource = App.getNetworkDataSource(mApplication.getApplicationContext());

            final FeedListRepository repository = new FeedRepositoryImpl(mFeedDataSource, mNewsDataSource,
                    mPreferenceDataSource, mNetworkDataSource);
            final FeedInteractor interactor = new FeedInteractorImpl(repository);

            return (T) new FeedListFragmentViewModel(interactor);
        }
        return super.create(modelClass);
    }
}
