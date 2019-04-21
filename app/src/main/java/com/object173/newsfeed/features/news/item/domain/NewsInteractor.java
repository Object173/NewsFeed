package com.object173.newsfeed.features.news.item.domain;

import com.object173.newsfeed.features.base.model.local.News;

import androidx.lifecycle.LiveData;

public interface NewsInteractor {
    LiveData<News> getNews(Long newsId);
}
