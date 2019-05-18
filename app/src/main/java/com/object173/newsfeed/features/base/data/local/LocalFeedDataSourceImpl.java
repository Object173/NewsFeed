package com.object173.newsfeed.features.base.data.local;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.base.model.local.Feed;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

public class LocalFeedDataSourceImpl implements LocalFeedDataSource {

    private final AppDatabase mDatabase;

    public LocalFeedDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public LiveData<Feed> getFeedAsync(String feedLink) {
        return Transformations.map(mDatabase.feedDao().getByIdAsync(feedLink), LocalFeedDataSourceImpl::convertToFeed);
    }

    @Override
    public Feed getFeed(String feedLink) {
        FeedDB feedDB = mDatabase.feedDao().getById(feedLink);
        return feedDB != null ? convertToFeed(feedDB) : null;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mDatabase.feedDao().getAllDataSource().map(feedDB -> {
            Feed feed = convertToFeed(feedDB.feed);
            feed.setNotReviewedCount(feedDB.notReviewedCount);
            return feed;
        });
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource(String category) {
        return mDatabase.feedDao().getByCategory(category).map(feedDB -> {
            Feed feed = convertToFeed(feedDB.feed);
            feed.setNotReviewedCount(feedDB.notReviewedCount);
            return feed;
        });
    }

    @Override
    public List<String> getFeedsByCategory(String category) {
        if(category != null) {
            return mDatabase.feedDao().getListByCategory(category);
        }
        return mDatabase.feedDao().getListByCategory();
    }

    @Override
    public List<String> getAutoUpdatedList() {
        return mDatabase.feedDao().getAutoUpdatedList();
    }

    @Override
    public boolean insertFeed(Feed feed) {
        if(mDatabase.feedDao().isExist(feed.getLink()) > 0) {
            return false;
        }
        FeedDB feedDB = convertToFeedDB(feed, feed.getIsCustomName() ? feed.getCustomName() : null,
                feed.isAutoRefresh(), feed.getCategory());
        return mDatabase.feedDao().insert(feedDB) > 0;
    }

    @Override
    public boolean updateFeed(Feed feed) {
        FeedDB feedDB = convertToFeedDB(feed, feed.getIsCustomName() ? feed.getCustomName() : null,
                feed.isAutoRefresh(), feed.getCategory());
        return mDatabase.feedDao().updateFeed(feedDB) > 0;
    }

    @Override
    public boolean removeFeed(String feedLink) {
        return mDatabase.feedDao().delete(feedLink) > 0;
    }

    @Override
    public boolean isExist(String feedLink) {
        return mDatabase.feedDao().isExist(feedLink) > 0;
    }

    private static Feed convertToFeed(FeedDB feedDB) {
        if(feedDB == null) {
            return null;
        }
        return new Feed(feedDB.link, feedDB.title, feedDB.description, feedDB.sourceLink,
                feedDB.updated, feedDB.iconLink, feedDB.author, feedDB.customName,
                feedDB.isAutoRefresh, feedDB.category);
    }

    private static FeedDB convertToFeedDB(Feed feed, String customName,
                                          boolean isAutoUpdate, String category) {
        return FeedDB.create(feed.getLink(), feed.getTitle(), feed.getDescription(), feed.getSourceLink(),
                feed.getUpdated(), feed.getIconLink(), feed.getAuthor(), customName,
                isAutoUpdate, category);
    }
}
