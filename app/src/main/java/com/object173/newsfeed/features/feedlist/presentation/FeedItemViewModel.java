package com.object173.newsfeed.features.feedlist.presentation;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.object173.newsfeed.features.feedlist.domain.model.Feed;
import com.object173.newsfeed.features.newslist.presentation.NewsListActivity;

public class FeedItemViewModel extends BaseObservable {

    private Feed feed;

    public void setFeed(final Feed feed) {
        this.feed = feed;
        notifyChange();
    }

    public String getTitle() {
        return feed.getTitle();
    }

    public String getUpdated() {
        return feed.getUpdated() != null ? feed.getUpdated().toString() : null;
    }

    public String getDescription() {
        return feed.getDescription();
    }

    public void onClick(final View view) {
        final Context context = view.getContext();
        final Intent intent = NewsListActivity.getIntent(context, feed.getLink());
        context.startActivity(intent);
    }
}
