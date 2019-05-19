package com.object173.newsfeed.db.dao;

import com.object173.newsfeed.db.converter.DateConverter;
import com.object173.newsfeed.db.entities.NewsDB;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM newsdb WHERE id=:id")
    LiveData<NewsDB> get(Long id);

    @Query("SELECT * FROM newsdb ORDER BY pubDate DESC")
    DataSource.Factory<Integer, NewsDB> getAll();

    @Transaction
    @Query("SELECT * FROM newsdb WHERE feedLink IN (SELECT FeedDB.link FROM feeddb WHERE category=:category) ORDER BY pubDate DESC")
    DataSource.Factory<Integer, NewsDB> getByCategory(String category);

    @Query("SELECT * FROM newsdb WHERE not isHidden ORDER BY pubDate DESC")
    DataSource.Factory<Integer, NewsDB> getAllDataSource();

    @Query("SELECT * FROM newsdb WHERE feedLink = :feedLink and not isHidden ORDER BY pubDate DESC")
    DataSource.Factory<Integer, NewsDB> getAllDataSource(String feedLink);

    @Query("SELECT COUNT(*) FROM newsdb WHERE feedLink=:feedLink and not isReviewed")
    LiveData<Integer> getNotReviewedCount(String feedLink);

    @Query("UPDATE newsdb SET isHidden=:isHidden WHERE id=:id ")
    int setHidden(long id, boolean isHidden);

    @Query("UPDATE newsdb SET isReviewed=1 WHERE id IN (:ids) AND not isReviewed")
    int checkReviewed(List<Long> ids);

    @Query("DELETE FROM newsdb WHERE feedLink = :feedLink and id NOT IN " +
            "(SELECT id FROM newsdb WHERE feedLink=:feedLink ORDER BY pubDate DESC LIMIT :cacheSize)")
    void cropCount(String feedLink, int cacheSize);

    @TypeConverters({DateConverter.class})
    @Query("DELETE FROM newsdb WHERE feedLink = :feedLink and pubDate<:date")
    void cropDate(String feedLink, Date date);

    @TypeConverters({DateConverter.class})
    @Query("SELECT * FROM newsdb WHERE feedLink=:feedLink AND pubDate=:pubDate")
    int isExist(String feedLink, Date pubDate);

    @Insert
    long insert(NewsDB news);
}
