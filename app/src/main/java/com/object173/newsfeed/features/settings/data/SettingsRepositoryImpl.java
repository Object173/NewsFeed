package com.object173.newsfeed.features.settings.data;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.settings.domain.SettingsRepository;
import com.object173.newsfeed.features.settings.domain.model.Feed;
import com.object173.newsfeed.features.settings.domain.model.News;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SettingsRepositoryImpl implements SettingsRepository {

    private final LocalDataSource mDataSource;

    public SettingsRepositoryImpl(LocalDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public List<Feed> getAutoUpdatedFeeds() {
        List<Feed> result = new LinkedList<>();
        for(FeedDB feedDb : mDataSource.getAutoUpdatedFeeds()) {
            result.add(convertToFeed(feedDb));
        }
        return result;
    }

    @Override
    public void setFeedUpdated(String feedLink, Date updated) {
        mDataSource.setFeedUpdated(feedLink, updated);
    }

    @Override
    public int refreshNews(List<News> newsList, int cacheSize, Date cropDate) {
        List<NewsDB> insertedNews = new LinkedList<>();
        for(News news : newsList) {
            insertedNews.add(convertToNewsDB(news));
        }
        return mDataSource.refreshNews(insertedNews, cacheSize, cropDate);
    }

    private static Feed convertToFeed(FeedDB feedDB) {
        return new Feed(feedDB.link, feedDB.title,
                feedDB.description, feedDB.sourceLink,
                feedDB.updated, feedDB.iconLink, feedDB.author, feedDB.customName,
                feedDB.isAutoRefresh, feedDB.category);
    }

    private static NewsDB convertToNewsDB(final News news) {
        return NewsDB.create(news.getId(), news.getFeedLink(), news.getTitle(), news.getDescription(),
                news.getPubDate(), news.getSourceLink());
    }
}
