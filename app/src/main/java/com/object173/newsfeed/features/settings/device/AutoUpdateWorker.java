package com.object173.newsfeed.features.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.object173.newsfeed.App;
import com.object173.newsfeed.R;
import com.object173.newsfeed.features.settings.data.LocalDataSource;
import com.object173.newsfeed.features.settings.data.LocalDataSourceImpl;
import com.object173.newsfeed.features.settings.data.SettingsRepositoryImpl;
import com.object173.newsfeed.features.settings.device.model.AutoUpdateConfig;
import com.object173.newsfeed.features.settings.domain.SettingsInteractor;
import com.object173.newsfeed.features.settings.domain.SettingsInteractorImpl;
import com.object173.newsfeed.features.settings.domain.SettingsRepository;
import com.object173.newsfeed.features.settings.domain.model.Feed;
import com.object173.newsfeed.features.settings.domain.model.News;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;
import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
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
        LOGGER.info("Update start");

        Downloader<FeedDTO> downloader = App.getDownloader(getApplicationContext());
        final LocalDataSource localDataSource = new LocalDataSourceImpl(App.getDatabase(getApplicationContext()));
        final SettingsRepository repository = new SettingsRepositoryImpl(localDataSource);
        final SettingsInteractor interactor = new SettingsInteractorImpl(repository);

        Context context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int cacheSize = preferences.getInt(context.getString(R.string.pref_key_cache_size),
                context.getResources().getInteger(R.integer.cache_size_default));
        int cacheFrequency = preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                context.getResources().getInteger(R.integer.cache_frequency_default));
        Date dateCrop = getCropDate(cacheFrequency);

        final List<Feed> feedList = interactor.getAutoUpdatedFeeds();
        int countFeed = 0;
        int countNews = 0;

        final StateUpdateNotification progressNotification =
                new StateUpdateNotification(getApplicationContext(), feedList.size());

        for(int i = 0; i < feedList.size(); i++) {
            if(isStopped()) {
                return Result.success();
            }
            progressNotification.setCurrentProgress(getApplicationContext(), i);

            final Feed feed = feedList.get(i);
            final String feedLink = feed.getLink();
            LOGGER.info("Update " + feedLink);
            final Response<FeedDTO> response = downloader.downloadObject(feedLink);

            if (response.result != Response.Result.Success) {
                LOGGER.info("Update fail " + response.result);
                interactor.setUpdatedFail(feedLink);
                continue;
            }

            final FeedDTO feedDTO = response.body;
            interactor.setUpdatedFeed(feedLink, new Date());

            List<News> newsList = new LinkedList<>();
            for (NewsDTO newsDTO : feedDTO.getNewsList()) {
                newsList.add(convertNews(feedLink, newsDTO));
            }
            countNews += interactor.refreshNews(newsList, cacheSize, dateCrop);

            countFeed++;
        }

        StateUpdateNotification.hide(getApplicationContext());
        UpdateResultNotification.show(getApplicationContext(), countFeed, countNews);

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
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = preferences.getBoolean(context.getString(R.string.pref_key_auto_refresh_enabled),
                context.getResources().getBoolean(R.bool.pref_auto_refresh_default));

        int updateInterval = preferences.getInt(context.getString(R.string.pref_key_refresh_period),
                context.getResources().getInteger(R.integer.cache_frequency_default));

        boolean isWifiOnly = preferences.getBoolean(context.getString(R.string.pref_key_wifi_only),
                context.getResources().getBoolean(R.bool.pref_wifi_only_default));

        return new AutoUpdateConfig(isEnabled, updateInterval, isWifiOnly);
    }
}
