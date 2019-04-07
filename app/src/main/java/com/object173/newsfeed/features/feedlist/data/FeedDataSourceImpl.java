package com.object173.newsfeed.features.feedlist.data;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import androidx.paging.DataSource;

public class FeedDataSourceImpl implements FeedDataSource {

    private final AppDatabase mDatabase;

    public FeedDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mDatabase.feedDao().getFeedList().map(feed -> convertToFeed(feed.feed, feed.notReviewedCount));
    }

    @Override
    public int removeFeed(final String feedLink) {
        return mDatabase.feedDao().delete(feedLink);
    }

    private static Feed convertToFeed(final FeedDB feedDB, final int notReviewedCount) {
        return new Feed(feedDB.link, feedDB.customName != null ? feedDB.customName : feedDB.title,
                feedDB.description, feedDB.updated, feedDB.iconLink, notReviewedCount);
    }
}
