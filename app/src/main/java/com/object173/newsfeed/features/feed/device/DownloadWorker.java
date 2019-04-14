package com.object173.newsfeed.features.feed.device;

import android.content.Context;
import android.content.SharedPreferences;

import com.object173.newsfeed.App;
import com.object173.newsfeed.R;
import com.object173.newsfeed.features.feed.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.feed.data.LocalDataSource;
import com.object173.newsfeed.features.feed.data.LocalDataSourceImpl;
import com.object173.newsfeed.features.feed.domain.FeedInteractor;
import com.object173.newsfeed.features.feed.domain.FeedInteractorImpl;
import com.object173.newsfeed.features.feed.domain.FeedRepository;
import com.object173.newsfeed.features.feed.domain.model.Feed;
import com.object173.newsfeed.features.feed.domain.model.RequestResult;
import com.object173.newsfeed.features.feed.domain.model.News;
import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;
import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DownloadWorker extends Worker {

    private static final ILogger LOGGER = LoggerFactory.get(DownloadWorker.class);

    private static final String DATA_KEY_FEED_LINK = "feed_link";
    private static final String DATA_KEY_CUSTOM_NAME = "custom_name";
    private static final String DATA_KEY_IS_AUTO_UPDATE = "auto_update";
    private static final String DATA_KEY_CATEGORY = "category";

    private static final String DATA_KEY_RESULT = "result";

    public static LiveData<RequestResult> startLoadFeed(final Feed feed) {
        final WorkRequest request = DownloadWorker.getRequest(feed.getLink(), feed.getCustomName(),
                feed.isAutoRefresh(), feed.getCategory());
        final UUID requestId = request.getId();

        final LiveData<RequestResult> result = Transformations.map(WorkManager.getInstance()
                .getWorkInfoByIdLiveData(requestId), DownloadWorker::getResult);

        WorkManager.getInstance().enqueue(request);

        return result;
    }

    private static WorkRequest getRequest(final String feedLink, final String customName,
                                  final boolean isAutoUpdate, final String category) {
        final Data inputData = new Data.Builder()
                .putString(DATA_KEY_FEED_LINK, feedLink)
                .putBoolean(DATA_KEY_IS_AUTO_UPDATE, isAutoUpdate)
                .putString(DATA_KEY_CATEGORY, category)
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

    private static RequestResult getResult(WorkInfo info) {
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
        final Downloader<FeedDTO> downloader = App.getDownloader(getApplicationContext());

        final String feedLink = getInputData().getString(DATA_KEY_FEED_LINK);
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

        final LocalDataSource dataSource = new LocalDataSourceImpl(App
                .getDatabase(getApplicationContext()));
        final FeedRepository repository = new FeedRepositoryImpl(dataSource);
        final FeedInteractor feedInteractor = new FeedInteractorImpl(repository);

        final FeedDTO feedDTO = response.body;
        final String customName = getInputData().getString(DATA_KEY_CUSTOM_NAME);
        final boolean isAutoUpdate = getInputData().getBoolean(DATA_KEY_IS_AUTO_UPDATE, true);
        final String category = getInputData().getString(DATA_KEY_CATEGORY);

        if(!feedInteractor.insertFeed(convertFeed(feedLink, feedDTO, customName, isAutoUpdate, category))) {
            return createFailResult(RequestResult.EXISTS);
        }

        final List<News> newsList = new LinkedList<>();
        for(NewsDTO newsDTO : feedDTO.getNewsList()) {
            newsList.add(convertNews(newsDTO, feedLink));
        }

        Context context = getApplicationContext();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int cacheSize = preferences.getInt(context.getString(R.string.pref_key_cache_size),
                context.getResources().getInteger(R.integer.cache_size_default));
        int cacheFrequency = preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                context.getResources().getInteger(R.integer.cache_frequency_default));

        feedInteractor.insertNews(newsList, cacheSize, getCropDate(cacheFrequency));

        return Result.success();
    }

    private static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }

    private static Feed convertFeed(String feedLink, FeedDTO feedDTO, String customName,
                                    boolean isAutoUpdate, String category) {
        return new Feed(feedLink, feedDTO.getTitle(), feedDTO.getDescription(),
                feedDTO.getSourceLink(), new Date(), feedDTO.getIconLink(), feedDTO.getAuthor(),
                customName, isAutoUpdate, category);
    }

    private static News convertNews(final NewsDTO newsDTO, final String feedLink) {
        return new News(newsDTO.getId(), feedLink, newsDTO.getTitle(),
                newsDTO.getDescription(), newsDTO.getPublicationDate(), newsDTO.getSourceLink());
    }

    private Result createFailResult(RequestResult result) {
        final Data output = new Data.Builder()
                .putString(DATA_KEY_RESULT, result.name())
                .build();
        return Result.failure(output);
    }
}
