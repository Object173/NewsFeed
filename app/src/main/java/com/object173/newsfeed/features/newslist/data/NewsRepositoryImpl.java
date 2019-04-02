package com.object173.newsfeed.features.newslist.data;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.DataSource;

import com.object173.newsfeed.features.feedloader.data.DownloadWorker;
import com.object173.newsfeed.features.newslist.domain.NewsRepository;
import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import java.util.UUID;

import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class NewsRepositoryImpl implements NewsRepository {

    private final NewsDataSource mFeedDataSource;

    public NewsRepositoryImpl(NewsDataSource feedDataSource) {
        mFeedDataSource = feedDataSource;
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource() {
        return mFeedDataSource.getNewsDataSource();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource(String feedLink) {
        return mFeedDataSource.getNewsDataSource(feedLink);
    }

    @Override
    public LiveData<RequestResult> updateFeed(String feedLink) {
        final WorkRequest request = UpdateFeedWorker.getUpdateNow(feedLink);
        final UUID requestId = request.getId();

        final LiveData<RequestResult> result = Transformations.map(WorkManager.getInstance()
                .getWorkInfoByIdLiveData(requestId), UpdateFeedWorker::getResult);

        WorkManager.getInstance().enqueue(request);

        return result;
    }
}
