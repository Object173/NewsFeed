package com.object173.newsfeed.features.news.domain;

import com.object173.newsfeed.features.news.domain.model.News;

import androidx.lifecycle.LiveData;

public interface NewsInteractor {
    LiveData<News> getNews(Long newsId);
}
