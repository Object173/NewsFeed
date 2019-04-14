package com.object173.newsfeed.db.entities;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = CategoryDB.class, parentColumns = "title",
        childColumns = "category", onUpdate = CASCADE, onDelete = SET_NULL))
public class FeedDB {

    @PrimaryKey
    @ColumnInfo(index = true)
    @NonNull
    public String link;

    public String title;
    public String description;
    public String sourceLink;

    @TypeConverters({DateConverter.class})
    public Date updated;

    public String iconLink;
    public String author;

    public String customName;
    public boolean isAutoRefresh;
    public String category;

    public static FeedDB create(@NonNull String link, String title, String description,
                                String sourceLink, Date updated, String iconLink,
                                String author, String category) {
        final FeedDB result = new FeedDB();
        result.link = link;
        result.title = title;
        result.description = description;
        result.sourceLink = sourceLink;
        result.updated = updated;
        result.iconLink = iconLink;
        result.author = author;
        result.category = category;
        return result;
    }

    public static FeedDB create(@NonNull String link, String title, String description,
                                String sourceLink, Date updated, String iconLink,
                                String author, String customName, boolean isAutoRefresh, String category) {
        final FeedDB result = create(link, title, description, sourceLink, updated, iconLink, author, category);
        result.customName = customName;
        result.isAutoRefresh = isAutoRefresh;
        return result;
    }

    public static class FeedWithReviewed {
        @Embedded
        public FeedDB feed;

        public int notReviewedCount;
    }
}
