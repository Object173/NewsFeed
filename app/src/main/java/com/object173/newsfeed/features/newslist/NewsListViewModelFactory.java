package com.object173.newsfeed.features.newslist;

import android.app.Application;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.NewsRepository;
import com.object173.newsfeed.features.newslist.category.domain.NewsCategoryInteractor;
import com.object173.newsfeed.features.newslist.category.domain.NewsCategoryInteractorImpl;
import com.object173.newsfeed.features.newslist.category.presentation.NewsCategoryViewModel;
import com.object173.newsfeed.features.newslist.feed.domain.NewsFeedInteractor;
import com.object173.newsfeed.features.newslist.feed.domain.NewsFeedInteractorImpl;
import com.object173.newsfeed.features.newslist.feed.presentation.NewsFeedViewModel;

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

            final NewsRepository newsRepository = App.getNewsRepository(mApplication.getApplicationContext());
            final FeedRepository feedRepository = App.getFeedRepository(mApplication.getApplicationContext());
            final NewsFeedInteractor newsInteractor = new NewsFeedInteractorImpl(newsRepository, feedRepository);

            return (T) new NewsFeedViewModel(newsInteractor);
        }
        if(modelClass == NewsCategoryViewModel.class) {

            final NewsRepository newsRepository = App.getNewsRepository(mApplication.getApplicationContext());
            final FeedRepository feedRepository = App.getFeedRepository(mApplication.getApplicationContext());
            final NewsCategoryInteractor newsInteractor = new NewsCategoryInteractorImpl(newsRepository, feedRepository);

            return (T) new NewsCategoryViewModel(newsInteractor);
        }
        return super.create(modelClass);
    }
}
