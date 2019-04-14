package com.object173.newsfeed.features.settings.data;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;

import java.util.Date;
import java.util.List;

public class LocalDataSourceImpl implements LocalDataSource {

    private final AppDatabase mDatabase;

    public LocalDataSourceImpl(AppDatabase database) {
        mDatabase = database;
    }

    @Override
    public List<FeedDB> getAutoUpdatedFeeds() {
        return mDatabase.feedDao().getAutoUpdated();
    }

    @Override
    public void setFeedUpdated(String feedLink, Date updated) {
        mDatabase.feedDao().setUpdated(feedLink, updated);
    }

    @Override
    public int refreshNews(List<NewsDB> newsList, int cacheSize, Date cropDate) {
        if(newsList.isEmpty()) {
            return 0;
        }

        String feedLink = newsList.get(0).feedLink;
        int count = 0;

        for(int i = 0; i < newsList.size() && i < cacheSize; i++) {
            NewsDB newsDB = newsList.get(i);
            if(isNewsExist(feedLink, newsDB.pubDate) ||
                    newsDB.pubDate.getTime() <= cropDate.getTime()) {
                break;
            }
            mDatabase.newsDao().insert(newsDB);
            count++;
        }

        mDatabase.newsDao().cropDate(feedLink, cropDate);
        mDatabase.newsDao().cropCount(feedLink, cacheSize);

        return count;
    }

    private boolean isNewsExist(String feedLink, Date pubDate) {
        return mDatabase.newsDao().isExist(feedLink, pubDate) > 0;
    }
}
