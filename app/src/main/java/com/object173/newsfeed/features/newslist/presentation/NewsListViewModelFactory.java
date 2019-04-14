package com.object173.newsfeed.features.newslist.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.newslist.data.LocalDataSource;
import com.object173.newsfeed.features.newslist.data.LocalDataSourceImpl;
import com.object173.newsfeed.features.newslist.data.NewsRepositoryImpl;
import com.object173.newsfeed.features.newslist.domain.NewsInteractor;
import com.object173.newsfeed.features.newslist.domain.NewsInteractorImpl;
import com.object173.newsfeed.features.newslist.domain.NewsRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewsListViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;
    private final String mFeedLink;
    private final String mCategory;

    NewsListViewModelFactory(@NonNull Application application, String feedLink, String category) {
        super(application);
        mApplication = application;
        mFeedLink = feedLink;
        mCategory = category;
    }

    static NewsListViewModelFactory getByAll(@NonNull Application application) {
        return new NewsListViewModelFactory(application, null, null);
    }

    static NewsListViewModelFactory getByFeed(@NonNull Application application, String feedLink) {
        return new NewsListViewModelFactory(application, feedLink, null);
    }

    static NewsListViewModelFactory getByCategory(@NonNull Application application, String category) {
        return new NewsListViewModelFactory(application, null, category);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NewsListViewModel.class) {

            final LocalDataSource dataSource = new LocalDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final NewsRepository repository = new NewsRepositoryImpl(dataSource);
            final NewsInteractor newsInteractor = new NewsInteractorImpl(repository);

            return (T) new NewsListViewModel(newsInteractor, mFeedLink, mCategory);
        }
        return super.create(modelClass);
    }
}
