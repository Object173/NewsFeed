package com.object173.newsfeed.features.feedlist.domain.model;

import java.util.Date;

public class Feed {
    private final String link;
    private final String title;
    private final String description;
    private final Date updated;
    private final String iconLink;
    private final int notReviewedCount;

    public Feed(String link, String title, String description, Date updated, String iconLink, int notReviewedCount) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.updated = updated;
        this.iconLink = iconLink;
        this.notReviewedCount = notReviewedCount;
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

    public Date getUpdated() {
        return updated;
    }

    public String getIconLink() {
        return iconLink;
    }

    public int getNotReviewedCount() {
        return notReviewedCount;
    }
}
