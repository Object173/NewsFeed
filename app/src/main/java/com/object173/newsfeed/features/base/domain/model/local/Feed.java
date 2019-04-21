package com.object173.newsfeed.features.base.domain.model.local;

import java.util.Date;

public class Feed {
    private String link;
    private final String title;
    private final String description;
    private final String sourceLink;
    private final Date updated;
    private final String iconLink;
    private final String author;

    private String customName;
    private boolean isCustomName;
    private boolean isAutoRefresh;

    private String category;
    private int notReviewedCount;

    public Feed() {
        this.link = null;
        this.title = null;
        this.description = null;
        this.sourceLink = null;
        this.updated = null;
        this.iconLink = null;
        this.author = null;
        this.isAutoRefresh = true;
    }

    public Feed(String link, String title, String description, String sourceLink, Date updated,
                String iconLink, String author) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.sourceLink = sourceLink;
        this.updated = updated;
        this.iconLink = iconLink;
        this.author = author;
    }

    public Feed(String link, String title, String description, String sourceLink, Date updated,
                String iconLink, String author, String customName, boolean isAutoRefresh, String category) {
        this.link = link;
        this.title = title;
        this.description = description;
        this.sourceLink = sourceLink;
        this.updated = updated;
        this.iconLink = iconLink;
        this.author = author;
        this.customName = customName;
        this.isAutoRefresh = isAutoRefresh;
        this.isCustomName = customName != null;
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getCustomName() {
        return customName;
    }

    public boolean getIsCustomName() {
        return isCustomName;
    }

    public boolean isAutoRefresh() {
        return isAutoRefresh;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public void setIsCustomName(boolean isCustomName) {
        this.isCustomName = isCustomName;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        isAutoRefresh = autoRefresh;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCustomName(boolean customName) {
        isCustomName = customName;
    }

    public int getNotReviewedCount() {
        return notReviewedCount;
    }

    public void setNotReviewedCount(int notReviewedCount) {
        this.notReviewedCount = notReviewedCount;
    }
}
