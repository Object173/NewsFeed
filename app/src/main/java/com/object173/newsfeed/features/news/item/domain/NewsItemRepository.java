package com.object173.newsfeed.features.news.item.domain;

import androidx.lifecycle.LiveData;

import com.object173.newsfeed.features.base.model.local.News;

public interface NewsItemRepository {
    LiveData<News> getNews(Long id);
}
