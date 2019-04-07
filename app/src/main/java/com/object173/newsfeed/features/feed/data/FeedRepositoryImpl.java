package com.object173.newsfeed.features.feed.data;

import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.features.feed.domain.FeedRepository;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;

import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class FeedRepositoryImpl implements FeedRepository {

    private final LocalDataSource mLocalDataSource;

    public FeedRepositoryImpl(LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public LiveData<Feed> getFeed(final String feedLink) {
        return mLocalDataSource.getFeed(feedLink);
    }

    @Override
    public LiveData<RequestResult> loadFeed(final Feed feed) {
        final WorkRequest request = DownloadWorker.getRequest(feed.getLink(), feed.getCustomName(),
                feed.isAutoRefresh(), feed.isMainChannel());
        final UUID requestId = request.getId();

        final LiveData<RequestResult> result = Transformations.map(WorkManager.getInstance()
                .getWorkInfoByIdLiveData(requestId), DownloadWorker::getResult);

        WorkManager.getInstance().enqueue(request);

        return result;
    }

    @Override
    public LiveData<RequestResult> updateFeed(Feed feed) {
        return mLocalDataSource.updateFeed(convertFeed(feed));
    }

    private static FeedDB convertFeed(Feed feed) {
        return FeedDB.create(feed.getLink(), feed.getTitle(), feed.getDescription(), feed.getSourceLink(),
                feed.getUpdated(), feed.getIconLink(), feed.getAuthor(), feed.isCustomName() ? feed.getCustomName() : null,
                feed.isAutoRefresh(), feed.isMainChannel());
    }
}
