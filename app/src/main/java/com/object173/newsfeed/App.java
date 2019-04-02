package com.object173.newsfeed;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.object173.newsfeed.db.AppDatabase;
import com.object173.newsfeed.libs.network.ResponseParser;
import com.object173.newsfeed.libs.parser.FeedParserFactory;

public class App extends Application {

    private AppDatabase database;
    private ResponseParser feedParser;

    public static AppDatabase getDatabase(final Context context) {
        return getApp(context).database;
    }

    public static ResponseParser getFeedParser(final Context context) {
        return getApp(context).feedParser;
    }

    private static App getApp(final Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();

        feedParser = FeedParserFactory.getFeedParser();
    }
}
