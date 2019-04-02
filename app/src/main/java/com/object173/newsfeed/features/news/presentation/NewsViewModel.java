package com.object173.newsfeed.features.news.presentation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.object173.newsfeed.features.news.domain.NewsInteractor;
import com.object173.newsfeed.features.news.domain.model.News;

class NewsViewModel extends ViewModel {

    private final NewsInteractor mNewsInteractor;

    private final Long mNewId;
    private final LiveData<News> mNews;

    NewsViewModel(final NewsInteractor newsInteractor, final Long newsId) {
        mNewsInteractor = newsInteractor;
        mNewId = newsId;

        mNews = mNewsInteractor.getNews(mNewId);
    }

    LiveData<News> getNews() {
        return mNews;
    }
}
