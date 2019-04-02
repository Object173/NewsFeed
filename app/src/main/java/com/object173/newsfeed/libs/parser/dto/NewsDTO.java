package com.object173.newsfeed.libs.parser.dto;

import java.util.Date;

public interface NewsDTO {
    Long getId();
    String feedLink();
    String getTitle();
    String getDescription();
    Date getPublicationDate();
    String getSourceLink();
}
