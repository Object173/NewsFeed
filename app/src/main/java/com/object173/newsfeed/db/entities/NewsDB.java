package com.object173.newsfeed.db.entities;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = FeedDB.class, parentColumns = "link",
        childColumns = "feedLink", onDelete = CASCADE))
public class NewsDB {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    public Long id;

    public String feedLink;
    public String title;
    public String description;

    @TypeConverters({DateConverter.class})
    public Date pubDate;

    public String sourceLink;

    public boolean isHidden;
    public boolean isReviewed;

    public static NewsDB create(Long id, String feedLink, String title, String description,
                  Date pubDate, String sourceLink) {
        final NewsDB result = new NewsDB();
        result.id = id;
        result.feedLink = feedLink;
        result.title = title;
        result.description = description;
        result.pubDate = pubDate;
        result.sourceLink = sourceLink;
        return result;
    }
}
