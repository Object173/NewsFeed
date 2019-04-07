package com.object173.newsfeed.features.newslist.data;

import com.object173.newsfeed.features.newslist.domain.NewsRepository;
import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;

import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class NewsRepositoryImpl implements NewsRepository {

    private final LocalDataSource mLocalDataSource;

    public NewsRepositoryImpl(LocalDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public DataSource.Factory<Integer, News> getLocalDataSource() {
        return mLocalDataSource.getNewsDataSource();
    }

    @Override
    public DataSource.Factory<Integer, News> getNewsDataSource(String feedLink) {
        return mLocalDataSource.getNewsDataSource(feedLink);
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

    @Override
    public LiveData<Boolean> hideNews(long id) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        new Thread(() -> {
            result.postValue(mLocalDataSource.hideNews(id) > 0);
        }).start();
        return result;
    }

    @Override
    public void checkReviewed(long id) {
        new Thread(() -> mLocalDataSource.checkReviewed(id)).start();
    }
}
