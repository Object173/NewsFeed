package com.object173.newsfeed.db.dao;

import com.object173.newsfeed.db.entities.FeedDB;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface FeedDao {

    @Transaction
    @Query("SELECT * FROM feeddb WHERE link = :feedLink ORDER BY updated DESC")
    LiveData<FeedDB> getByIdAsync(String feedLink);

    @Transaction
    @Query("SELECT * FROM feeddb WHERE link = :feedLink ORDER BY updated DESC")
    FeedDB getById(String feedLink);

    @Transaction
    @Query("SELECT COUNT(*) FROM feeddb WHERE link=:feedLink")
    int isExist(String feedLink);

    @Transaction
    @Query("SELECT FeedDB.*, (SELECT COUNT(*) FROM newsdb WHERE feedLink=feeddb.link and not isReviewed) " +
            "as notReviewedCount FROM feeddb ORDER BY updated DESC")
    DataSource.Factory<Integer, FeedDB.FeedWithReviewed> getAllDataSource();

    @Transaction
    @Query("SELECT FeedDB.*, (SELECT COUNT(*) FROM newsdb WHERE feedLink=feeddb.link and not isReviewed) " +
            "as notReviewedCount FROM feeddb WHERE category=:category ORDER BY updated DESC")
    DataSource.Factory<Integer, FeedDB.FeedWithReviewed> getByCategory(String category);

    @Transaction
    @Query("SELECT feeddb.link FROM feeddb WHERE isAutoRefresh ORDER BY updated")
    List<String> getAutoUpdatedList();

    @Transaction
    @Query("SELECT feeddb.link FROM feeddb WHERE category=:category ORDER BY updated")
    List<String> getListByCategory(String category);

    @Transaction
    @Query("SELECT feeddb.link FROM feeddb ORDER BY updated")
    List<String> getListByCategory();

    @Insert
    long insert(FeedDB feed);

    @Transaction
    @Query("DELETE FROM feeddb WHERE link=:feedLink")
    int delete(String feedLink);

    @Update
    int updateFeed(FeedDB feedDB);
}
