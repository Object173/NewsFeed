package com.object173.newsfeed.features.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Pair;

import com.object173.newsfeed.App;
import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.model.local.News;
import com.object173.newsfeed.features.base.domain.model.network.RequestResult;
import com.object173.newsfeed.features.base.domain.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.domain.model.pref.NotificationConfig;
import com.object173.newsfeed.features.settings.domain.AutoUpdateService;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;
import com.object173.newsfeed.features.base.data.network.dto.NewsDTO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

    public static void start(final Context context) {
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
        FeedRepository feedRepository = App.getFeedRepository(getApplicationContext());
        List<String> feedList = feedRepository.getAutoUpdateList();

        final StateUpdateNotification progressNotification =
                new StateUpdateNotification(getApplicationContext(), feedList.size());

        int countFeed = 0;
        int countNews = 0;

        for(int i = 0; i < feedList.size(); i++) {
            if(isStopped()) {
                return Result.success();
            }
            progressNotification.setCurrentProgress(getApplicationContext(), i);

            Pair<RequestResult, Integer> result = feedRepository.updateFeedNews(feedList.get(i));

            if(result.first == RequestResult.SUCCESS) {
                countNews += result.second;
                countFeed++;
            }
        }

        StateUpdateNotification.hide(getApplicationContext());

        NotificationConfig notificationConfig = App.getConfigRepository(getApplicationContext()).getNotificationConfig();
        UpdateResultNotification.show(getApplicationContext(), notificationConfig, countFeed, countNews);

        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();

        StateUpdateNotification.hide(getApplicationContext());
        start(getApplicationContext());
    }

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }

    private static News convertNews(final String feedLink, final NewsDTO newsDTO) {
        return new News(newsDTO.getId(), feedLink, newsDTO.getTitle(),
                newsDTO.getDescription(), newsDTO.getPublicationDate(), newsDTO.getSourceLink());
    }

    private static AutoUpdateConfig getConfig(final Context context) {
        return App.getConfigRepository(context).getAutoUpdateConfig();
    }
}
