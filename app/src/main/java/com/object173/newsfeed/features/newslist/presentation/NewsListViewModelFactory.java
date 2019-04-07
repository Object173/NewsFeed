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

    NewsListViewModelFactory(@NonNull Application application, String feedLink) {
        super(application);
        mApplication = application;
        mFeedLink = feedLink;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NewsListViewModel.class) {

            final LocalDataSource dataSource = new LocalDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final NewsRepository repository = new NewsRepositoryImpl(dataSource);
            final NewsInteractor newsInteractor = new NewsInteractorImpl(repository);

            return (T) new NewsListViewModel(newsInteractor, mFeedLink);
        }
        return super.create(modelClass);
    }
}
