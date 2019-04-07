package com.object173.newsfeed;

import android.app.Application;
import android.content.Context;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.network.DownloaderFactory;
import com.object173.newsfeed.libs.parser.FeedParserFactory;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;

import androidx.room.Room;

public class App extends Application {

    private AppDatabase database;
    private Downloader<FeedDTO> downloader;

    public static AppDatabase getDatabase(final Context context) {
        return getApp(context).database;
    }

    public static Downloader<FeedDTO> getDownloader(final Context context) {
        return getApp(context).downloader;
    }

    private static App getApp(final Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();

        downloader  = DownloaderFactory.get(FeedParserFactory.getFeedParser());
    }
}
