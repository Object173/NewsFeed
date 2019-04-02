package com.object173.newsfeed.features.feedlist.data;

import android.arch.paging.DataSource;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.feedlist.domain.model.Feed;

public class FeedDataSourceImpl implements FeedDataSource {

    private final AppDatabase mDatabase;

    public FeedDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public DataSource.Factory<Integer, Feed> getFeedDataSource() {
        return mDatabase.feedDao().getAllDataSource().map(FeedDataSourceImpl::convertToFeed);
    }

    private static Feed convertToFeed(FeedDB feedDto) {
        return new Feed(feedDto.getLink(), feedDto.getTitle(), feedDto.getDescription(),
                feedDto.getSourceLink(), feedDto.getUpdated(), feedDto.getIconLink(),
                feedDto.getAuthor());
    }
}
