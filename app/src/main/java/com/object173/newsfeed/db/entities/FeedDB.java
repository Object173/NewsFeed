package com.object173.newsfeed.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.parser.dto.NewsDTO;

import java.util.Date;
import java.util.List;

@Entity
public class FeedDB implements FeedDTO {

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

    public static FeedDB create(@NonNull String link, String title, String description,
                                String sourceLink, Date updated, String iconLink,
                                String author) {
        final FeedDB result = new FeedDB();
        result.link = link;
        result.title = title;
        result.description = description;
        result.sourceLink = sourceLink;
        result.updated = updated;
        result.iconLink = iconLink;
        result.author = author;
        return result;
    }

    @Override
    @NonNull
    public String getLink() {
        return link;
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
    public String getSourceLink() {
        return sourceLink;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public String getIconLink() {
        return iconLink;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public List<NewsDTO> getNewsList() {
        return null;
    }
}
