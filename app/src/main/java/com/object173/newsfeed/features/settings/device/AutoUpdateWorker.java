package com.object173.newsfeed.features.settings.device;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSourceImpl;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSourceImpl;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.features.base.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.model.pref.NotificationConfig;
import com.object173.newsfeed.features.settings.data.UpdateRepositoryImpl;
import com.object173.newsfeed.features.settings.domain.AutoUpdateService;
import com.object173.newsfeed.features.settings.domain.UpdateRepository;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoUpdateWorker extends Worker {

    private static final ILogger LOGGER = LoggerFactory.get(AutoUpdateWorker.class);
    private static final int UPDATE_PERIOD_DIFF = 30;
    static final String WORKER_TAG = "auto_update_worker";

    public static class AutoUpdateServiceImpl implements AutoUpdateService {
        @Override
        public void start(Context context) {
            AutoUpdateWorker.start(context);
        }
    }

    public AutoUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private static void start(final Context context) {
        final AutoUpdateConfig config = getConfig(context);
        LOGGER.info("setConfig " + config.enabled + " " + config.updateInterval);

        if(config.enabled) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(config.wifiOnly ? NetworkType.UNMETERED : NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                    .Builder(AutoUpdateWorker.class, config.updateInterval, TimeUnit.HOURS,
                        UPDATE_PERIOD_DIFF, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag(WORKER_TAG)
                    .build();

            WorkManager.getInstance().enqueueUniquePeriodicWork(WORKER_TAG, ExistingPeriodicWorkPolicy.REPLACE, workRequest);
        }
        else {
            WorkManager.getInstance().cancelAllWorkByTag(WORKER_TAG);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        UpdateRepository updateRepository = getRepository();
        List<String> feedList = updateRepository.getAutoUpdateList();

        final StateUpdateNotification progressNotification =
                new StateUpdateNotification(getApplicationContext(), feedList.size());

        int countFeed = 0;
        int countNews = 0;

        for(int i = 0; i < feedList.size(); i++) {
            if(isStopped()) {
                return Result.success();
            }
            progressNotification.setCurrentProgress(getApplicationContext(), i);

            Pair<RequestResult, Integer> result = updateRepository.updateFeedNews(feedList.get(i));

            if(result.first == RequestResult.SUCCESS) {
                countNews += result.second;
                countFeed++;
            }
        }

        StateUpdateNotification.hide(getApplicationContext());

        NotificationConfig notificationConfig = App.getPreferenceDataSource(getApplicationContext()).getNotificationConfig();
        UpdateResultNotification.show(getApplicationContext(), notificationConfig, countFeed, countNews);

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();

        StateUpdateNotification.hide(getApplicationContext());
        start(getApplicationContext());
    }

    private UpdateRepository getRepository() {
        final AppDatabase database = App.getDatabase(getApplicationContext());

        final LocalNewsDataSource mNewsDataSource = new LocalNewsDataSourceImpl(database);
        final LocalFeedDataSource mFeedDataSource = new LocalFeedDataSourceImpl(database);
        final PreferenceDataSource mPreferenceDataSource = App.getPreferenceDataSource(getApplicationContext());
        final NetworkDataSource mNetworkDataSource = App.getNetworkDataSource(getApplicationContext());

        return new UpdateRepositoryImpl(mNewsDataSource, mFeedDataSource,
                mNetworkDataSource, mPreferenceDataSource);
    }

    private static AutoUpdateConfig getConfig(final Context context) {
        return App.getPreferenceDataSource(context).getAutoUpdateConfig();
    }
}
