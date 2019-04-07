package com.object173.newsfeed.features.newslist.data;

import android.content.Context;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;
import com.object173.newsfeed.libs.network.Response;

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

public class UpdateFeedWorker extends Worker {

    private static final ILogger LOGGER = LoggerFactory.get(UpdateFeedWorker.class);

    private static final String DATA_KEY_FEED_LINK = "feed_link";
    private static final String DATA_KEY_RESULT = "result";

    static WorkRequest getUpdateNow(final String feedLink) {
        final Data inputData = new Data.Builder()
                .putString(DATA_KEY_FEED_LINK, feedLink)
                .build();

        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        return new OneTimeWorkRequest.Builder(UpdateFeedWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
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

    public UpdateFeedWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        LOGGER.info("doWork");
        final NetworkDataSource networkDataSource = new NetworkDataSourceImpl(App.getDownloader(getApplicationContext()));

        String feedLink = getInputData().getString(DATA_KEY_FEED_LINK);
        final Response<FeedDTO> response = networkDataSource.loadFeed(feedLink);

        if(response.result != Response.Result.Success) {
            LOGGER.info(feedLink + " " + response.result.toString() + " " + response.httpCode);
            switch (response.result) {
                case HTTP_ERROR: return createFailResult(RequestResult.HTTP_FAIL);
                case CONNECTION_ERROR: return createFailResult(RequestResult.CONNECT_FAIL);
                case PARSE_ERROR: return createFailResult(RequestResult.INCORRECT_RESPONSE);
                default: return createFailResult(RequestResult.FAIL);
            }
        }

        final LocalDataSource dataSource = new LocalDataSourceImpl(App.getDatabase(getApplicationContext()));

        final FeedDTO feedDTO = response.body;
        if(!dataSource.isFeedExists(feedLink)) {
            return createFailResult(RequestResult.NOT_EXISTS);
        }
        dataSource.setFeedUpdated(feedLink, new Date());

        List<NewsDB> newsList = new LinkedList<>();
        for(NewsDTO newsDTO : feedDTO.getNewsList()) {
            if(!dataSource.isExist(feedLink, newsDTO.getPublicationDate())) {
                newsList.add(convertNews(feedLink, newsDTO));
            }
        }

        int cacheSize = dataSource.getCacheSize(getApplicationContext());
        int cacheFrequency =  dataSource.getCacheFrequency(getApplicationContext());
        dataSource.refreshNews(feedLink, newsList, cacheSize, getCropDate(cacheFrequency));

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

    private Result createFailResult(RequestResult result) {
        final Data output = new Data.Builder()
                .putString(DATA_KEY_RESULT, result.name())
                .build();
        return Result.failure(output);
    }
}
