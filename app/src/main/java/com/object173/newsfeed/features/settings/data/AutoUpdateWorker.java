package com.object173.newsfeed.features.settings.data;

import android.content.Context;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.settings.domain.model.AutoUpdateConfig;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;
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
    private static final String WORKER_TAG = "auto_update_worker";

    public AutoUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    static void setConfig(final AutoUpdateConfig config) {
        LOGGER.info("setConfig " + config.enabled + " " + config.updateInterval);

        if(config.enabled) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(config.wifiOnly ? NetworkType.UNMETERED : NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                    .Builder(AutoUpdateWorker.class, config.updateInterval, TimeUnit.HOURS)
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

        final NetworkDataSource networkDataSource = new NetworkDataSourceImpl(App.getDownloader(getApplicationContext()));
        final LocalDataSource localDataSource = new LocalDataSourceImpl(App.getDatabase(getApplicationContext()));
        final PreferencesDataSource preferencesDataSource = new PreferencesDataSourceImpl();

        final int cacheSize = preferencesDataSource.getCacheSize(getApplicationContext());
        final Date dateCrop = getCropDate(preferencesDataSource.getCacheFrequency(getApplicationContext()));

        for(FeedDB feedDB : localDataSource.getAutoUpdatedFeeds()) {
            final String feedLink = feedDB.link;
            LOGGER.info("Update " + feedLink);
            final Response<FeedDTO> response = networkDataSource.loadFeed(feedLink);

            if (response.result != Response.Result.Success) {
                LOGGER.info("Update fail " + response.result);
                localDataSource.setFeedUpdated(feedLink, null);
                continue;
            }

            final FeedDTO feedDTO = response.body;
            localDataSource.setFeedUpdated(feedLink, new Date());

            List<NewsDB> newsList = new LinkedList<>();
            for (NewsDTO newsDTO : feedDTO.getNewsList()) {
                if (!localDataSource.isNewsExist(feedLink, newsDTO.getPublicationDate())) {
                    newsList.add(convertNews(feedLink, newsDTO));
                }
            }
            localDataSource.refreshNews(feedLink, newsList, cacheSize, dateCrop);
        }

        return Result.success();
    }

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }

    private static NewsDB convertNews(final String feedLink, final NewsDTO newsDTO) {
        return NewsDB.create(newsDTO.getId(), feedLink, newsDTO.getTitle(),
                newsDTO.getDescription(), newsDTO.getPublicationDate(), newsDTO.getSourceLink());
    }
}
