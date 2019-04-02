package com.object173.newsfeed.libs.parser.dto;

import java.util.Date;
import java.util.List;

public interface FeedDTO {
    String getLink();
    String getTitle();
    String getDescription();
    String getSourceLink();
    Date getUpdated();
    String getIconLink();
    String getAuthor();
    List<NewsDTO> getNewsList();
}
