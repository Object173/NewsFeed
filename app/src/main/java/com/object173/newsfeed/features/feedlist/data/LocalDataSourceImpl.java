package com.object173.newsfeed.features.feedlist.data;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.CategoryDB;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.feedlist.domain.model.Category;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;

public class LocalDataSourceImpl implements LocalDataSource {

    private final AppDatabase mDatabase;

    public LocalDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mDatabase.feedDao().getFeedList().map(feed -> convertToFeed(feed.feed, feed.notReviewedCount));
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource(String category) {
        return mDatabase.feedDao().getByCategory(category).map(feed -> convertToFeed(feed.feed, feed.notReviewedCount));
    }

    @Override
    public int removeFeed(final String feedLink) {
        return mDatabase.feedDao().delete(feedLink);
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return Transformations.map(mDatabase.categoryDao().getCategories(), input -> {
            List<Category> result = new LinkedList<>();
            for(CategoryDB categoryDB : input) {
                result.add(convertCategory(categoryDB));
            }
            return result;
        });
    }

    private static Feed convertToFeed(final FeedDB feedDB, final int notReviewedCount) {
        return new Feed(feedDB.link, feedDB.customName != null ? feedDB.customName : feedDB.title,
                feedDB.description, feedDB.updated, feedDB.iconLink, notReviewedCount);
    }

    private static Category convertCategory(CategoryDB categoryDB) {
        return new Category(categoryDB.title);
    }
}
