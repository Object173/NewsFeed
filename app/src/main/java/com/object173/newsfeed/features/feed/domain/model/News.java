package com.object173.newsfeed.features.feed.domain.model;

import java.util.Date;

public class News {
    private final Long id;
    private final String feedLink;
    private final String title;
    private final String description;
    private final Date pubDate;
    private final String sourceLink;

    public News(Long id, String feedLink, String title, String description, Date pubDate, String sourceLink) {
        this.id = id;
        this.feedLink = feedLink;
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.sourceLink = sourceLink;
    }

    public Long getId() {
        return id;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getSourceLink() {
        return sourceLink;
    }
}
