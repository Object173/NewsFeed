package com.object173.newsfeed.features.newslist.presentation;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.newslist.data.NewsDataSource;
import com.object173.newsfeed.features.newslist.data.NewsDataSourceImpl;
import com.object173.newsfeed.features.newslist.data.NewsRepositoryImpl;
import com.object173.newsfeed.features.newslist.domain.NewsInteractor;
import com.object173.newsfeed.features.newslist.domain.NewsInteractorImpl;
import com.object173.newsfeed.features.newslist.domain.NewsRepository;

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

            final NewsDataSource dataSource = new NewsDataSourceImpl(App
                    .getDatabase(mApplication.getApplicationContext()));
            final NewsRepository repository = new NewsRepositoryImpl(dataSource);
            final NewsInteractor newsInteractor = new NewsInteractorImpl(repository);

            return (T) new NewsListViewModel(newsInteractor, mFeedLink);
        }
        return super.create(modelClass);
    }
}
