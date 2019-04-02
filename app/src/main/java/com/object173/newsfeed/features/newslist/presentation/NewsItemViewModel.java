package com.object173.newsfeed.features.newslist.presentation;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.object173.newsfeed.features.news.presentation.NewsActivity;
import com.object173.newsfeed.features.newslist.domain.model.News;

public class NewsItemViewModel extends BaseObservable {

    private News mNews;

    public void setNews(final News news) {
        this.mNews = news;
        notifyChange();
    }

    public String getTitle() {
        return mNews.getTitle();
    }

    public String getUpdated() {
        return mNews.getPubDate() != null ? mNews.getPubDate().toString() : null;
    }

    public String getDescription() {
        return mNews.getDescription();
    }

    public void onClick(final View view) {
        final Context context = view.getContext();
        final Intent intent = NewsActivity.getIntent(context, mNews.getId());
        context.startActivity(intent);
    }
}
