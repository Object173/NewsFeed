package com.object173.newsfeed;

import android.app.Application;
import android.content.Context;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.features.base.data.CategoryRepositoryImpl;
import com.object173.newsfeed.features.base.data.ConfigRepositoryImpl;
import com.object173.newsfeed.features.base.data.FeedRepositoryImpl;
import com.object173.newsfeed.features.base.data.NewsRepositoryImpl;
import com.object173.newsfeed.features.base.data.local.LocalCategoryDataSource;
import com.object173.newsfeed.features.base.data.local.LocalCategoryDataSourceImpl;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSource;
import com.object173.newsfeed.features.base.data.local.LocalFeedDataSourceImpl;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSource;
import com.object173.newsfeed.features.base.data.local.LocalNewsDataSourceImpl;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSourceImpl;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSourceImpl;
import com.object173.newsfeed.features.base.domain.CategoryRepository;
import com.object173.newsfeed.features.base.domain.ConfigRepository;
import com.object173.newsfeed.features.base.domain.FeedRepository;
import com.object173.newsfeed.features.base.domain.NewsRepository;

import androidx.room.Room;

public class App extends Application {

    private AppDatabase mDatabase;

    private FeedRepository mFeedRepository;
    private NewsRepository mNewsRepository;
    private CategoryRepository mCategoryRepository;
    private ConfigRepository mConfigRepository;

    public static AppDatabase getDatabase(final Context context) {
        return getApp(context).mDatabase;
    }

    public static FeedRepository getFeedRepository(final Context context) {
        return getApp(context).mFeedRepository;
    }

    public static NewsRepository getNewsRepository(final Context context) {
        return getApp(context).mNewsRepository;
    }

    public static CategoryRepository getCategoryRepository(final Context context) {
        return getApp(context).mCategoryRepository;
    }

    public static ConfigRepository getConfigRepository(final Context context) {
        return getApp(context).mConfigRepository;
    }

    private static App getApp(final Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();

        LocalFeedDataSource feedDataSource = new LocalFeedDataSourceImpl(mDatabase);
        LocalNewsDataSource newsDataSource = new LocalNewsDataSourceImpl(mDatabase);
        LocalCategoryDataSource categoryDataSource = new LocalCategoryDataSourceImpl(mDatabase);
        PreferenceDataSource preferenceDataSource = new PreferenceDataSourceImpl(getApplicationContext());
        NetworkDataSource networkDataSource = new NetworkDataSourceImpl();

        mFeedRepository = new FeedRepositoryImpl(feedDataSource, newsDataSource, preferenceDataSource, networkDataSource);
        mNewsRepository = new NewsRepositoryImpl(newsDataSource);
        mCategoryRepository = new CategoryRepositoryImpl(categoryDataSource);
        mConfigRepository = new ConfigRepositoryImpl(preferenceDataSource);
    }
}
