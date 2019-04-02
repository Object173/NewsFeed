package com.object173.newsfeed.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.object173.newsfeed.db.entities.NewsDB;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM newsdb WHERE id=:id")
    LiveData<NewsDB> get(Long id);

    @Query("SELECT * FROM newsdb ORDER BY pubDate")
    LiveData<List<NewsDB>> getAll();

    @Query("SELECT * FROM newsdb WHERE feedLink = :feedLink ORDER BY pubDate")
    LiveData<List<NewsDB>> getById(String feedLink);

    @Query("SELECT * FROM newsdb ORDER BY pubDate")
    DataSource.Factory<Integer, NewsDB> getAllDataSource();

    @Query("SELECT * FROM newsdb WHERE feedLink = :feedLink ORDER BY pubDate")
    DataSource.Factory<Integer, NewsDB> getAllDataSource(String feedLink);

    @Query("DELETE FROM newsdb WHERE feedLink = :feedLink")
    void clear(String feedLink);

    @Insert
    void insert(NewsDB news);

    @Update
    void update(NewsDB news);

    @Delete
    void delete(NewsDB news);

}
