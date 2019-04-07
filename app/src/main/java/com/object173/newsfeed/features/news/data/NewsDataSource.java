package com.object173.newsfeed.features.news.data;

import com.object173.newsfeed.features.news.domain.model.News;

import androidx.lifecycle.LiveData;

public interface NewsDataSource {
    LiveData<News> getNews(Long id);
}
