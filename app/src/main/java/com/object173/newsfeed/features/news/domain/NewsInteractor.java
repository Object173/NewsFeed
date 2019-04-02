package com.object173.newsfeed.features.news.domain;

import android.arch.lifecycle.LiveData;

import com.object173.newsfeed.features.news.domain.model.News;

public interface NewsInteractor {
    LiveData<News> getNews(Long newsId);
}
