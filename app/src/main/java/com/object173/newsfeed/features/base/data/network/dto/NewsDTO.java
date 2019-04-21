package com.object173.newsfeed.features.base.data.network.dto;

import java.util.Date;

public interface NewsDTO {
    Long getId();
    String feedLink();
    String getTitle();
    String getDescription();
    Date getPublicationDate();
    String getSourceLink();
}
