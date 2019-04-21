package com.object173.newsfeed.features.news.presentation;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.domain.NewsRepository;
import com.object173.newsfeed.features.news.domain.NewsInteractor;
import com.object173.newsfeed.features.news.domain.NewsInteractorImpl;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NewsViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private final Application mApplication;
    private final Long mNewsId;

    NewsViewModelFactory(@NonNull Application application, Long newsId) {
        super(application);
        mApplication = application;
        mNewsId = newsId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NewsViewModel.class) {

            final NewsRepository repository = App.getNewsRepository(mApplication.getApplicationContext());
            final NewsInteractor newsInteractor = new NewsInteractorImpl(repository);

            return (T) new NewsViewModel(newsInteractor, mNewsId);
        }
        return super.create(modelClass);
    }
}
