package com.object173.newsfeed.features.settings.data;

import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;

public interface NetworkDataSource {
    Response<FeedDTO> loadFeed(String feedLink);
}
