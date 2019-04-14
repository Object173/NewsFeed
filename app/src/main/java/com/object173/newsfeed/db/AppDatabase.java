package com.object173.newsfeed.db;

import com.object173.newsfeed.db.dao.CategoryDao;
import com.object173.newsfeed.db.dao.FeedDao;
import com.object173.newsfeed.db.dao.NewsDao;
import com.object173.newsfeed.db.entities.CategoryDB;
import com.object173.newsfeed.db.entities.FeedDB;
import com.object173.newsfeed.db.entities.NewsDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FeedDB.class, NewsDB.class, CategoryDB.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FeedDao feedDao();
    public abstract NewsDao newsDao();
    public abstract CategoryDao categoryDao();
}
