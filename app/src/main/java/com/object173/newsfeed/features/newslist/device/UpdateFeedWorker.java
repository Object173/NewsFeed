package com.object173.newsfeed.features.newslist.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.object173.newsfeed.App;
import com.object173.newsfeed.R;
import com.object173.newsfeed.db.entities.NewsDB;
import com.object173.newsfeed.features.newslist.data.LocalDataSource;
import com.object173.newsfeed.features.newslist.data.LocalDataSourceImpl;
import com.object173.newsfeed.features.newslist.data.NewsRepositoryImpl;
import com.object173.newsfeed.features.newslist.domain.NewsInteractor;
import com.object173.newsfeed.features.newslist.domain.NewsInteractorImpl;
import com.object173.newsfeed.features.newslist.domain.NewsRepository;
import com.object173.newsfeed.features.newslist.domain.model.News;
import com.object173.newsfeed.features.newslist.domain.model.RequestResult;
import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;
import com.object173.newsfeed.libs.log.ILogger;
import com.object173.newsfeed.libs.log.LoggerFactory;
import com.object173.newsfeed.libs.network.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateFeedWorker extends Worker {

    private static final ILogger LOGGER = LoggerFactory.get(UpdateFeedWorker.class);

    private static final String DATA_KEY_FEED_LINK = "feed_link";
    private static final String DATA_KEY_CATEGORY = "category";
    private static final String DATA_KEY_RESULT = "result";

    public static LiveData<RequestResult> startByFeed(final String feedLink) {
        final WorkRequest request = getRequest(feedLink, null);
        final UUID requestId = request.getId();

        final LiveData<RequestResult> result = Transformations.map(WorkManager.getInstance()
                .getWorkInfoByIdLiveData(requestId), UpdateFeedWorker::getResult);

        WorkManager.getInstance().enqueue(request);

        return result;
    }

    public static LiveData<RequestResult> startByCategory(final String category) {
        final WorkRequest request = getRequest(null, category);
        final UUID requestId = request.getId();

        final LiveData<RequestResult> result = Transformations.map(WorkManager.getInstance()
                .getWorkInfoByIdLiveData(requestId), UpdateFeedWorker::getResult);

        WorkManager.getInstance().enqueue(request);

        return result;
    }

    private static WorkRequest getRequest(final String feedLink, final String category) {
        final Data inputData = new Data.Builder()
                .putString(DATA_KEY_FEED_LINK, feedLink)
                .putString(DATA_KEY_CATEGORY, category)
                .build();

        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        return new OneTimeWorkRequest.Builder(UpdateFeedWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
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

    public UpdateFeedWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        LOGGER.info("doWork");
        final LocalDataSource localDataSource = new LocalDataSourceImpl(App.getDatabase(getApplicationContext()));
        final NewsRepository repository = new NewsRepositoryImpl(localDataSource);
        final NewsInteractor interactor = new NewsInteractorImpl(repository);

        final Downloader<FeedDTO> downloader = App.getDownloader(getApplicationContext());

        String category = getInputData().getString(DATA_KEY_CATEGORY);
        String feed = getInputData().getString(DATA_KEY_FEED_LINK);

        String feedLinks[] = feed != null ? new String[] {feed} :
                (String[])interactor.getFeedsByCategory(category).toArray();

        if(feedLinks == null) {
            return Result.success();
        }

        for(String feedLink : feedLinks) {
            final Response<FeedDTO> response = downloader.downloadObject(feedLink);

            if (response.result != Response.Result.Success) {
                LOGGER.info(feedLink + " " + response.result.toString() + " " + response.httpCode);
                switch (response.result) {
                    case HTTP_ERROR:
                        return createFailResult(RequestResult.HTTP_FAIL);
                    case CONNECTION_ERROR:
                        return createFailResult(RequestResult.CONNECT_FAIL);
                    case PARSE_ERROR:
                        return createFailResult(RequestResult.INCORRECT_RESPONSE);
                    default:
                        return createFailResult(RequestResult.FAIL);
                }
            }

            final FeedDTO feedDTO = response.body;
            interactor.setFeedUpdated(feedLink, new Date());

            List<News> newsList = new LinkedList<>();
            for (NewsDTO newsDTO : feedDTO.getNewsList()) {
                newsList.add(convertNews(feedLink, newsDTO));
            }

            Context context = getApplicationContext();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int cacheSize = preferences.getInt(context.getString(R.string.pref_key_cache_size),
                    context.getResources().getInteger(R.integer.cache_size_default));
            int cacheFrequency = preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                    context.getResources().getInteger(R.integer.cache_frequency_default));

            interactor.refreshNews(newsList, cacheSize, getCropDate(cacheFrequency));
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

    private static News convertNews(final String feedLink, final NewsDTO newsDTO) {
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
