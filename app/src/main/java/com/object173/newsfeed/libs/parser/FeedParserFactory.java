package com.object173.newsfeed.libs.parser;

import android.text.format.Time;

import com.object173.newsfeed.libs.network.ResponseParser;
import com.object173.newsfeed.libs.parser.dto.FeedDTO;
import com.object173.newsfeed.libs.xml.StringParser;
import com.object173.newsfeed.libs.xml.XmlObjectParser;
import com.object173.newsfeed.libs.xml.XmlObjectParserFactory;
import com.object173.newsfeed.libs.parser.conf.AtomFeed;
import com.object173.newsfeed.libs.parser.conf.RssFeed;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedParserFactory {

    private static FeedParserFactory instance;
    private final XmlObjectParser[] objectParsers;

    private FeedParserFactory() {
        final Map<Class<?>, StringParser> stringParserMap = new HashMap<>();
        stringParserMap.put(Date.class, input -> {
            final Time time = new Time();
            time.parse3339(input);
            return new Date(time.toMillis(false));
        });
        stringParserMap.put(Integer.class, Integer::parseInt);

        objectParsers = new XmlObjectParser[]{
                XmlObjectParserFactory.get(AtomFeed.class, stringParserMap),
                XmlObjectParserFactory.get(RssFeed.class, stringParserMap)
        };
    }

    private static FeedParserFactory getInstance() {
        if(instance == null) {
            instance = new FeedParserFactory();
        }
        return instance;
    }

    public static ResponseParser<FeedDTO> getFeedParser() {
        return new FeedParserImpl(getInstance().objectParsers);
    }
}
