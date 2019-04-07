package com.object173.newsfeed.libs.parser;

import com.object173.newsfeed.libs.network.ResponseParser;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.xml.StringParser;
import com.object173.newsfeed.libs.xml.XmlObjectParser;
import com.object173.newsfeed.libs.xml.XmlObjectParserFactory;
import com.object173.newsfeed.libs.parser.conf.AtomFeed;
import com.object173.newsfeed.libs.parser.conf.RssFeed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedParserFactory {

    private static XmlObjectParser[] getObjectParsers() {
        final Map<Class<?>, StringParser> stringParserMap = new HashMap<>();
        stringParserMap.put(Date.class, Date::new);
        stringParserMap.put(Integer.class, Integer::parseInt);

        return new XmlObjectParser[]{
                XmlObjectParserFactory.get(AtomFeed.class, stringParserMap),
                XmlObjectParserFactory.get(RssFeed.class, stringParserMap)
        };
    }

    public static ResponseParser<FeedDTO> getFeedParser() {
        return new FeedParserImpl(getObjectParsers());
    }
}
