package com.object173.newsfeed.db;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AppDatabaseFactory {

    private static AppDatabaseFactory instance;
    private AppDatabase database;

    private AppDatabaseFactory(final Context context) {
        database = Room.databaseBuilder(context, AppDatabase.class, "database").build();
    }

    public static AppDatabase getDb(final Context context) {
        if(instance == null) {
            instance = new AppDatabaseFactory(context);
        }
        return instance.database;
    }
}
