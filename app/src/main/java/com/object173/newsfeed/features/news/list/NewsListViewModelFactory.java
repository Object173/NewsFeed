package com.object173.newsfeed.features.news.list;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSourceImpl;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSourceImpl;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.feed.item.domain.FeedItemRepository;
import com.object173.newsfeed.features.news.item.domain.NewsItemRepository;
import com.object173.newsfeed.features.news.list.category.data.NewsCategoryRepositoryImpl;
import com.object173.newsfeed.features.news.list.category.domain.NewsCategoryInteractor;
import com.object173.newsfeed.features.news.list.category.domain.NewsCategoryInteractorImpl;
import com.object173.newsfeed.features.news.list.category.domain.NewsCategoryRepository;
import com.object173.newsfeed.features.news.list.category.presentation.NewsCategoryViewModel;
import com.object173.newsfeed.features.news.list.feed.data.NewsFeedRepositoryImpl;
import com.object173.newsfeed.features.news.list.feed.domain.NewsFeedInteractor;
import com.object173.newsfeed.features.news.list.feed.domain.NewsFeedInteractorImpl;
import com.object173.newsfeed.features.news.list.feed.domain.NewsFeedRepository;
import com.object173.newsfeed.features.news.list.feed.presentation.NewsFeedViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewsListViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;

    public NewsListViewModelFactory(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NewsFeedViewModel.class) {

            final AppDatabase database = App.getDatabase(mApplication.getApplicationContext());

            final LocalNewsDataSource mNewsDataSource = new LocalNewsDataSourceImpl(database);
            final LocalFeedDataSource mFeedDataSource = new LocalFeedDataSourceImpl(database);
            final PreferenceDataSource mPreferenceDataSource = App.getPreferenceDataSource(mApplication.getApplicationContext());
            final NetworkDataSource mNetworkDataSource = App.getNetworkDataSource(mApplication.getApplicationContext());

            final NewsFeedRepository newsRepository = new NewsFeedRepositoryImpl(mNewsDataSource, mFeedDataSource,
                    mPreferenceDataSource, mNetworkDataSource);
            final NewsFeedInteractor newsInteractor = new NewsFeedInteractorImpl(newsRepository);

            return (T) new NewsFeedViewModel(newsInteractor);
        }
        if(modelClass == NewsCategoryViewModel.class) {

            final AppDatabase database = App.getDatabase(mApplication.getApplicationContext());

            final LocalNewsDataSource mNewsDataSource = new LocalNewsDataSourceImpl(database);
            final LocalFeedDataSource mFeedDataSource = new LocalFeedDataSourceImpl(database);
            final PreferenceDataSource mPreferenceDataSource = App.getPreferenceDataSource(mApplication.getApplicationContext());
            final NetworkDataSource mNetworkDataSource = App.getNetworkDataSource(mApplication.getApplicationContext());

            final NewsCategoryRepository newsRepository = new NewsCategoryRepositoryImpl(mNewsDataSource, mFeedDataSource,
                    mPreferenceDataSource, mNetworkDataSource);
            final NewsCategoryInteractor newsInteractor = new NewsCategoryInteractorImpl(newsRepository);

            return (T) new NewsCategoryViewModel(newsInteractor);
        }
        return super.create(modelClass);
    }
}
