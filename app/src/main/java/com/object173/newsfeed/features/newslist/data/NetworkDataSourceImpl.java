package com.object173.newsfeed.features.newslist.data;

import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;

public class NetworkDataSourceImpl implements NetworkDataSource {

    private final Downloader<FeedDTO> mDownloader;

    public NetworkDataSourceImpl(Downloader<FeedDTO> downloader) {
        mDownloader = downloader;
    }

    @Override
    public Response<FeedDTO> loadFeed(final String feedLink) {
        return mDownloader.downloadObject(feedLink);
    }
}
