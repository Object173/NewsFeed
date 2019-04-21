package com.object173.newsfeed.features.base.data.network;

import com.object173.newsfeed.features.base.data.network.conf.AtomFeed;
import com.object173.newsfeed.features.base.data.network.conf.RssFeed;
import com.object173.newsfeed.features.base.data.network.dto.FeedDTO;
import com.object173.newsfeed.features.base.data.network.dto.NewsDTO;
import com.object173.newsfeed.features.base.model.local.Feed;
import com.object173.newsfeed.features.base.model.local.News;
import com.object173.newsfeed.features.base.model.network.RequestResult;
import com.object173.newsfeed.libs.network.Downloader;
import com.object173.newsfeed.libs.network.DownloaderFactory;
import com.object173.newsfeed.libs.network.Response;
import com.object173.newsfeed.libs.network.ResponseParser;
import com.object173.newsfeed.libs.parser.XmlResponseParser;
import com.object173.newsfeed.libs.parser.xml.StringParser;
import com.object173.newsfeed.libs.parser.xml.XmlObjectParser;
import com.object173.newsfeed.libs.parser.xml.XmlObjectParserFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NetworkDataSourceImpl implements NetworkDataSource {

    private final Downloader<FeedDTO> mDownloader;

    public NetworkDataSourceImpl() {
        final Map<Class<?>, StringParser> stringParserMap = new HashMap<>();
        stringParserMap.put(Date.class, Date::new);
        stringParserMap.put(Integer.class, Integer::parseInt);

        ResponseParser<FeedDTO> parser = new XmlResponseParser<>(new XmlObjectParser[]{
                XmlObjectParserFactory.get(AtomFeed.class, stringParserMap),
                XmlObjectParserFactory.get(RssFeed.class, stringParserMap)
        });

        mDownloader = DownloaderFactory.get(parser);
    }

    @Override
    public ResponseDTO getNewsFeed(String feedLink) {
        final Response<FeedDTO> response = mDownloader.downloadObject(feedLink);

        if(response.result != com.object173.newsfeed.libs.network.Response.Result.Success) {
            switch (response.result) {
                case HTTP_ERROR: return ResponseDTO.createFail(RequestResult.HTTP_FAIL);
                case CONNECTION_ERROR: return ResponseDTO.createFail(RequestResult.INCORRECT_LINK);
                case PARSE_ERROR: return ResponseDTO.createFail(RequestResult.INCORRECT_RESPONSE);
                default: return ResponseDTO.createFail(RequestResult.FAIL);
            }
        }

        final FeedDTO feedDTO = response.body;
        final Feed feed = convertFeed(feedLink, feedDTO);

        final List<News> newsList = new LinkedList<>();
        for(NewsDTO newsDTO : feedDTO.getNewsList()) {
            newsList.add(convertNews(newsDTO, feedLink));
        }

        return ResponseDTO.createSuccess(feed, newsList);
    }

    private static Feed convertFeed(String feedLink, FeedDTO feedDTO) {
        return new Feed(feedLink, feedDTO.getTitle(), feedDTO.getDescription(),
                feedDTO.getSourceLink(), new Date(), feedDTO.getIconLink(), feedDTO.getAuthor(),
                null, false, null);
    }

    private static News convertNews(final NewsDTO newsDTO, final String feedLink) {
        return new News(newsDTO.getId(), feedLink, newsDTO.getTitle(),
                newsDTO.getDescription(), newsDTO.getPublicationDate(), newsDTO.getSourceLink());
    }
}
