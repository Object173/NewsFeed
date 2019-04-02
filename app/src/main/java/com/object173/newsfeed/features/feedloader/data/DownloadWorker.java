package com.object173.newsfeed.features.feedloader.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.object173.newsfeed.App;
import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.feedloader.domain.model.RequestResult;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;
import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.network.DownloaderFactory;
import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;

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

    private static final String DATA_KEY_FEED_LINKS = "feed_links";
    private static final String DATA_KEY_RESULT = "result";

    static WorkRequest getUpdateNow(final String feedLinks) {
        return getUpdateNow(new String[] {feedLinks});
    }

    private static WorkRequest getUpdateNow(final String[] feedLinks) {
        final Data inputData = new Data.Builder()
                .putStringArray(DATA_KEY_FEED_LINKS, feedLinks)
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
        final Downloader downloader = DownloaderFactory.get(
                App.getFeedParser(getApplicationContext()));

        for(String feedLink : getInputData().getStringArray(DATA_KEY_FEED_LINKS)) {

            final Response<FeedDTO> response = downloader.downloadObject(feedLink);

            if(response.result != Response.Result.Success) {
                LOGGER.info(feedLink + " " + response.result.toString() + " " + response.httpCode);
                switch (response.result) {
                    case HTTP_ERROR: return createFailResult(RequestResult.HTTP_FAIL);
                    case CONNECTION_ERROR: return createFailResult(RequestResult.INCORRECT_LINK);
                    case PARSE_ERROR: return createFailResult(RequestResult.INCORRECT_RESPONSE);
                    default: return createFailResult(RequestResult.FAIL);
                }
            }

            final AppDatabase database = App.getDatabase(getApplicationContext());

            final FeedDTO feedDTO = response.body;

            if(database.feedDao().getById(feedLink) != null) {
                return createFailResult(RequestResult.EXISTS);
            }

            database.feedDao().insert(FeedDB.create(feedLink, feedDTO.getTitle(), feedDTO.getDescription(),
                    feedDTO.getSourceLink(), feedDTO.getUpdated(), feedDTO.getIconLink(), feedDTO.getAuthor()));

            for(NewsDTO newsDTO : feedDTO.getNewsList()) {
                final NewsDB newsDB = NewsDB.create(newsDTO.getId(), feedLink, newsDTO.getTitle(),
                        newsDTO.getDescription(), newsDTO.getPublicationDate(), newsDTO.getSourceLink());
                database.newsDao().insert(newsDB);
            }
        }

        return Result.success();
    }

    private Result createFailResult(RequestResult result) {
        final Data output = new Data.Builder()
                .putString(DATA_KEY_RESULT, result.name())
                .build();
        return Result.failure(output);
    }
}
