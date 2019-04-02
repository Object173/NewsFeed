package com.object173.newsfeed.features.feedlist.domain.model;

import java.util.Date;

public class Feed {
    private final String link;
    private final String title;
    private final String description;
    private final String sourceLink;
    private final Date updated;
    private final String iconLink;
    private final String author;

    public Feed(String link, String title, String description, String sourceLink, Date updated, String iconLink, String author) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.sourceLink = sourceLink;
        this.updated = updated;
        this.iconLink = iconLink;
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getIconLink() {
        return iconLink;
    }

    public String getAuthor() {
        return author;
    }
}
