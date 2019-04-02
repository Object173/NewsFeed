package com.object173.newsfeed.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.object173.newsfeed.db.entities.FeedDB;

import java.util.List;

@Dao
public interface FeedDao {

    @Transaction
    @Query("SELECT * FROM feeddb ORDER BY updated")
    List<FeedDB> getAll();

    @Transaction
    @Query("SELECT * FROM feeddb WHERE link = :link ORDER BY updated")
    FeedDB getById(String link);

    @Transaction
    @Query("SELECT * FROM feeddb ORDER BY updated")
    DataSource.Factory<Integer, FeedDB> getAllDataSource();

    @Insert
    void insert(FeedDB feed);

    @Update
    void update(FeedDB feed);

    @Delete
    void delete(FeedDB feed);

}
