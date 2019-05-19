package com.object173.newsfeed.features.feed.item.presentation;


import androidx.databinding.BaseObservable;

import com.object173.newsfeed.features.base.model.local.Feed;

import java.io.Serializable;

public class FeedBinding extends BaseObservable implements Serializable {

    private final Feed mFeed;
    private final boolean mIsNewFeed;

    FeedBinding(Feed feed, boolean isNewFeed) {
        mFeed = feed;
        mIsNewFeed = isNewFeed;
        notifyChange();
    }

    public Feed getFeed() {
        return mFeed;
    }

    public String getLink() {
        return mFeed.getLink();
    }

    public void setLink(String link) {
        mFeed.setLink(link);
    }

    public String getTitle() {
        return mFeed.getTitle();
    }

    public String getDescription() {
        return mFeed.getDescription();
    }

    public String getCustomName() {
        return mFeed.getCustomName();
    }

    public void setCustomName(String customName) {
        mFeed.setCustomName(customName);
    }

    public boolean getIsCustomName() {
        return mFeed.getIsCustomName();
    }

    public void setIsCustomName(boolean isCustomName) {
        mFeed.setIsCustomName(isCustomName);
        notifyChange();
    }

    public boolean getIsAutoRefresh() {
        return mFeed.isAutoRefresh();
    }

    public void setIsAutoRefresh(boolean autoRefresh) {
        mFeed.setAutoRefresh(autoRefresh);
    }

    public boolean isNewFeed() {
        return mIsNewFeed;
    }

    public String getCategory() {
        return mFeed.getCategory();
    }

    public void setCategory(String category) {
        mFeed.setCategory(category);
        notifyChange();
    }
}
