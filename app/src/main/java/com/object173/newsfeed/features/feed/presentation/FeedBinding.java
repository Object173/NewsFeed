package com.object173.newsfeed.features.feed.presentation;
import com.object173.newsfeed.features.feed.domain.model.Feed;

import androidx.databinding.BaseObservable;

public class FeedBinding extends BaseObservable {

    private final Feed mFeed;
    private final boolean mIsNewFeed;

    FeedBinding(Feed feed, boolean isNewFeed) {
        mFeed = feed;
        mIsNewFeed = isNewFeed;
        notifyChange();
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
        return mFeed.isCustomName();
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

    public boolean getIsMainChannel() {
        return mFeed.isMainChannel();
    }

    public void setIsMainChannel(boolean mainChannel) {
        mFeed.setMainChannel(mainChannel);
    }

    public boolean isNewFeed() {
        return mIsNewFeed;
    }
}
