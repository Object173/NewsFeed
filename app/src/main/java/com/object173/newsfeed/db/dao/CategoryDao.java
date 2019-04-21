package com.object173.newsfeed.db.dao;

import com.object173.newsfeed.db.entities.CategoryDB;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CategoryDao {
    @Insert
    void add(CategoryDB category);

    @Insert
    void insertAll(CategoryDB... categories);

    @Delete
    int remove(CategoryDB category);

    @Query("SELECT * FROM categorydb ORDER BY title")
    DataSource.Factory<Integer, CategoryDB> getAll();

    @Query("SELECT * FROM categorydb WHERE title=:title")
    CategoryDB getCategory(String title);

    @Query("SELECT * FROM categorydb ORDER BY title")
    LiveData<List<CategoryDB>> getCategories();
}
