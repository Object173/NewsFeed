package com.object173.newsfeed.features.feedloader.data;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.object173.newsfeed.features.feedloader.domain.LoaderRepository;
import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;

import java.util.UUID;

import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class LoaderRepositoryImpl implements LoaderRepository {

    @Override
    public LiveData<RequestResult> loadFeed(final String feedLink) {
        final WorkRequest request = DownloadWorker.getUpdateNow(feedLink);
        final UUID requestId = request.getId();

        final LiveData<RequestResult> result = Transformations.map(WorkManager.getInstance()
                .getWorkInfoByIdLiveData(requestId), DownloadWorker::getResult);

        WorkManager.getInstance().enqueue(request);

        return result;
    }
}
