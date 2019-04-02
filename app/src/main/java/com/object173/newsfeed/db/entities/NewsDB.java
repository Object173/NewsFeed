package com.object173.newsfeed.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.object173.newsfeed.libs.parser.dto.NewsDTO;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = FeedDB.class, parentColumns = "link",
        childColumns = "feedLink", onDelete = CASCADE))
public class NewsDB implements NewsDTO {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    public Long id;

    public String feedLink;
    public String title;
    public String description;
    @TypeConverters({DateConverter.class})
    public Date pubDate;
    public String sourceLink;

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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String feedLink() {
        return feedLink;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getPublicationDate() {
        return pubDate;
    }

    @Override
    public String getSourceLink() {
        return sourceLink;
    }
}
