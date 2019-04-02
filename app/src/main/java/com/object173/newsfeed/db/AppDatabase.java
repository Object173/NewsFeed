package com.object173.newsfeed.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.object173.newsfeed.db.dao.FeedDao;
import com.object173.newsfeed.db.dao.NewsDao;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;

@Database(entities = {FeedDB.class, NewsDB.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedDao feedDao();
    public abstract NewsDao newsDao();
}
