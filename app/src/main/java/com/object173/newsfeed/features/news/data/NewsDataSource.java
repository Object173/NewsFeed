package com.object173.newsfeed.features.news.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import com.object173.newsfeed.features.news.domain.model.News;

public interface NewsDataSource {
    LiveData<News> getNews(Long id);
}
