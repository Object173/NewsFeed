package com.object173.newsfeed.features.base.data.network.conf;

import android.text.format.Time;

import com.object173.newsfeed.features.base.data.network.dto.FeedDTO;
import com.object173.newsfeed.features.base.data.network.dto.NewsDTO;
import com.object173.newsfeed.libs.parser.xml.StringParser;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlField;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlMethod;
import com.object173.newsfeed.libs.parser.xml.annotations.XmlObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlObject(tag = "rss", verifyingKey = "version", verifyingValue = "2.0")
public final class RssFeed implements FeedDTO {

    @XmlField
    private FeedItem feed;

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public String getTitle() {
        return feed != null ? feed.title : null;
    }

    @Override
    public String getSourceLink() {
        return feed != null ? feed.sourceLink : null;
    }

    @Override
    public String getDescription() {
        return feed != null ? feed.description : null;
    }

    @Override
    public Date getUpdated() {
        return feed != null ? feed.updated : null;
    }

    @Override
    public String getIconLink() {
        return feed != null ? feed.getIconUrl() : null;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public List<NewsDTO> getNewsList() {
        return feed != null ? feed.newsList : null;
    }

    @XmlObject(tag = "channel")
    public static class FeedItem {
        @XmlField(tag = "title")
        private String title;
        @XmlField(tag = "link")
        private String sourceLink;
        @XmlField(tag = "description")
        private String description;
        @XmlField(tag="author")
        private String author;
        @XmlField(tag="lastBuildDate", parser = DateParser.class)
        private Date updated;
        @XmlField
        private Icon icon;

        private List<NewsDTO> newsList = new ArrayList<>();
        @XmlMethod
        public void addNews(FeedItem.NewsEntity news) {
            newsList.add(news);
        }

        String getIconUrl() {
            return icon != null ? icon.url : null;
        }

        @XmlObject(tag = "icon")
        public static final class Icon {
            @XmlField(tag = "url")
            private String url;
        }

        @XmlObject(tag = "item")
        public static final class NewsEntity implements NewsDTO {
            private String uuid;
            @XmlField(tag = "title")
            private String title;
            @XmlField(tag = "description")
            private String description;
            @XmlField(tag = "link")
            private String sourceLink;
            @XmlField(tag = "pubDate", parser = DateParser.class)
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
                return sourceLink;
            }
        }
    }

    public static class DateParser implements StringParser<Date> {
        @Override
        public Date parse(String input) {
            return new Date(input);
        }
    }
}
