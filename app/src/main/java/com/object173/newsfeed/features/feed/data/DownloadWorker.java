package com.object173.newsfeed.features.feed.data;

import android.content.Context;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;
import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DownloadWorker extends Worker {

    private static final ILogger LOGGER = LoggerFactory.get(DownloadWorker.class);

    private static final String DATA_KEY_FEED_LINK = "feed_link";
    private static final String DATA_KEY_CUSTOM_NAME = "custom_name";
    private static final String DATA_KEY_IS_AUTO_UPDATE = "auto_update";
    private static final String DATA_KEY_IS_MAIN_CHANNEL = "main_channel";

    private static final String DATA_KEY_RESULT = "result";

    static WorkRequest getRequest(final String feedLink, final String customName,
                                  final boolean isAutoUpdate, final boolean isMainChannel) {
        final Data inputData = new Data.Builder()
                .putString(DATA_KEY_FEED_LINK, feedLink)
                .putBoolean(DATA_KEY_IS_AUTO_UPDATE, isAutoUpdate)
                .putBoolean(DATA_KEY_IS_MAIN_CHANNEL, isMainChannel)
                .putString(DATA_KEY_CUSTOM_NAME, customName)
                .build();

        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        return new OneTimeWorkRequest.Builder(DownloadWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
    }

    public DownloadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    static RequestResult getResult(WorkInfo info) {
        switch (info.getState()) {
            case SUCCEEDED: return RequestResult.SUCCESS;
            case FAILED:
                String data = info.getOutputData().getString(DATA_KEY_RESULT);
                return RequestResult.valueOf(RequestResult.class, data);
            default: return RequestResult.RUNNING;
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        LOGGER.info("doWork");
        final NetworkDataSource networkDataSource = new NetworkDataSourceImpl(
                App.getDownloader(getApplicationContext()));

        final String feedLink = getInputData().getString(DATA_KEY_FEED_LINK);
        final Response<FeedDTO> response = networkDataSource.loadFeed(feedLink);

        if(response.result != Response.Result.Success) {
            LOGGER.info(feedLink + " " + response.result.toString() + " " + response.httpCode);
            switch (response.result) {
                case HTTP_ERROR: return createFailResult(RequestResult.HTTP_FAIL);
                case CONNECTION_ERROR: return createFailResult(RequestResult.INCORRECT_LINK);
                case PARSE_ERROR: return createFailResult(RequestResult.INCORRECT_RESPONSE);
                default: return createFailResult(RequestResult.FAIL);
            }
        }

        final LocalDataSource dataSource = new LocalDataSourceImpl(App.getDatabase(getApplicationContext()));

        final FeedDTO feedDTO = response.body;
        if(dataSource.isExists(feedLink)) {
            return createFailResult(RequestResult.EXISTS);
        }

        final String customName = getInputData().getString(DATA_KEY_CUSTOM_NAME);
        final boolean isAutoUpdate = getInputData().getBoolean(DATA_KEY_IS_AUTO_UPDATE, true);
        final boolean isMainChannel = getInputData().getBoolean(DATA_KEY_IS_MAIN_CHANNEL, true);

        final FeedDB feedDB = convertFeed(feedLink, feedDTO);
        feedDB.customName = customName;
        feedDB.isAutoRefresh = isAutoUpdate;
        feedDB.isMainChannel = isMainChannel;

        dataSource.insertFeed(feedDB);

        final List<NewsDB> newsList = new LinkedList<>();
        for(NewsDTO newsDTO : feedDTO.getNewsList()) {
            newsList.add(convertNews(newsDTO, feedLink));
        }

        int cacheSize = dataSource.getCacheSize(getApplicationContext());
        int cacheFrequency =  dataSource.getCacheFrequency(getApplicationContext());
        dataSource.insertNews(feedLink, newsList, cacheSize, getCropDate(cacheFrequency));

        return Result.success();
    }

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }

    private static FeedDB convertFeed(String feedLink, FeedDTO feedDTO) {
        return FeedDB.create(feedLink, feedDTO.getTitle(), feedDTO.getDescription(),
                feedDTO.getSourceLink(), new Date(), feedDTO.getIconLink(), feedDTO.getAuthor());
    }

    private static NewsDB convertNews(final NewsDTO newsDTO, final String feedLink) {
        return NewsDB.create(newsDTO.getId(), feedLink, newsDTO.getTitle(),
                newsDTO.getDescription(), newsDTO.getPublicationDate(), newsDTO.getSourceLink());
    }

    private Result createFailResult(RequestResult result) {
        final Data output = new Data.Builder()
                .putString(DATA_KEY_RESULT, result.name())
                .build();
        return Result.failure(output);
    }
}
