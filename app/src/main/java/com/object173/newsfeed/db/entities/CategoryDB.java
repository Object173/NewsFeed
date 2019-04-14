package com.object173.newsfeed.db.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryDB {

    @PrimaryKey
    @NonNull
    public String title;

    public static CategoryDB create(String title) {
        CategoryDB category = new CategoryDB();
        category.title = title;
        return category;
    }
}
