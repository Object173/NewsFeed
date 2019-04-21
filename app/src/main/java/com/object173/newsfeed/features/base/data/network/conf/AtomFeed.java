package com.object173.newsfeed.features.base.data.network.conf;

import com.object173.newsfeed.features.base.data.network.dto.FeedDTO;
import com.object173.newsfeed.features.base.data.network.dto.NewsDTO;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlAttribute;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlField;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlMethod;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlObject(tag = "feed")
public final class AtomFeed implements FeedDTO {

    @XmlField(tag = "title")
    private String title;
    @XmlField
    private Link sourceLink;
    @XmlField(tag = "description")
    private String description;
    @XmlField
    private Author author;
    @XmlField(tag = "icon")
    private String iconLink;
    @XmlField(tag = "updated")
    private Date updatedDate;
    private List<NewsDTO> newsList = new ArrayList<>();

    @XmlMethod
    private void addNews(NewsEntity news) {
        newsList.add(news);
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSourceLink() {
        return sourceLink != null ? sourceLink.link : null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getUpdated() {
        return updatedDate;
    }

    @Override
    public String getIconLink() {
        return iconLink;
    }

    @Override
    public String getAuthor() {
        return author != null ? author.name : null;
    }

    @Override
    public List<NewsDTO> getNewsList() {
        return newsList;
    }

    @XmlObject(tag = "link")
    public static final class Link {
        @XmlAttribute(key = "href")
        private String link;
    }

    @XmlObject(tag = "author")
    public static final class Author {
        @XmlAttribute(key = "name")
        private String name;
    }

    @XmlObject(tag = "entry")
    public static final class NewsEntity implements NewsDTO {
        private String uuid;
        @XmlField(tag = "title")
        private String title;
        @XmlField(tag = "summary")
        private String description;
        @XmlField(tag = "link")
        private Link sourceLink;
        @XmlField(tag = "updated")
        private Date publicationDate;

        @Override
        public Long getId() {
            return null;
        }

        @Override
        public String feedLink() {
            return null;
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
            return publicationDate;
        }

        @Override
        public String getSourceLink() {
            return sourceLink != null ? sourceLink.link : null;
        }
    }
}
