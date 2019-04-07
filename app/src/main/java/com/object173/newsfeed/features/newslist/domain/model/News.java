package com.object173.newsfeed.features.newslist.domain.model;

import java.util.Date;

public class News {
    private final Long id;
    private final String title;
    private final Date pubDate;
    //private final boolean isReviewed;

    public News(Long id, String title, Date pubDate, boolean isReviewed) {
        this.id = id;
        this.title = title;
        this.pubDate = pubDate;
        //this.isReviewed = isReviewed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    /*public boolean isReviewed() {
        return isReviewed;
    }*/
}
