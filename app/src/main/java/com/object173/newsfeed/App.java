package com.object173.newsfeed;

import android.app.Application;
import android.content.Context;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.features.base.data.network.NetworkDataSource;
import com.object173.newsfeed.features.base.data.network.NetworkDataSourceImpl;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.data.pref.PreferenceDataSourceImpl;

import androidx.room.Room;

public class App extends Application {

    private AppDatabase mDatabase;
    private NetworkDataSource mNetworkDataSource;
    private PreferenceDataSource mPreferenceDataSource;

    public static AppDatabase getDatabase(final Context context) {
        return getApp(context).mDatabase;
    }

    public static NetworkDataSource getNetworkDataSource(final Context context) {
        return getApp(context).mNetworkDataSource;
    }

    public static PreferenceDataSource getPreferenceDataSource(final Context context) {
        return getApp(context).mPreferenceDataSource;
    }

    private static App getApp(final Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabase = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();

        mNetworkDataSource = new NetworkDataSourceImpl(getApplicationContext());
        mPreferenceDataSource = new PreferenceDataSourceImpl(getApplicationContext());
    }
}
