package com.object173.newsfeed.db.dao;

import com.object173.newsfeed.db.entities.DateConverter;
import com.object173.newsfeed.db.entities.FeedDB;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

@Dao
public interface FeedDao {

    @Transaction
    @Query("SELECT * FROM feeddb ORDER BY updated")
    List<FeedDB> getAll();

    @Transaction
    @Query("SELECT * FROM feeddb WHERE link = :feedLink ORDER BY updated DESC")
    LiveData<FeedDB> getById(String feedLink);

    @Transaction
    @Query("SELECT COUNT(*) FROM feeddb WHERE link=:feedLink")
    int isExist(String feedLink);

    @Transaction
    @Query("SELECT * FROM feeddb ORDER BY updated DESC")
    DataSource.Factory<Integer, FeedDB> getAllDataSource();

    @Transaction
    @Query("SELECT FeedDB.*, (SELECT COUNT(*) FROM newsdb WHERE feedLink=feeddb.link and not isReviewed) " +
            "as notReviewedCount FROM feeddb ORDER BY updated DESC")
    DataSource.Factory<Integer, FeedDB.FeedWithReviewed> getFeedList();

    @Insert
    void insert(FeedDB feed);

    @TypeConverters({DateConverter.class})
    @Query("UPDATE feeddb SET updated=:updated WHERE link=:feedLink")
    void setUpdated(String feedLink, Date updated);

    @Transaction
    @Query("DELETE FROM feeddb WHERE link=:feedLink")
    int delete(String feedLink);

    @Update
    int updateFeed(FeedDB feedDB);
}
