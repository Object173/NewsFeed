package com.object173.newsfeed.libs.parser;

import android.util.Xml;

import com.object173.newsfeed.libs.network.ResponseParser;
import com.object173.newsfeed.libs.parser.xml.XmlObjectParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;

public class XmlResponseParser<T> implements ResponseParser<T> {

    private final XmlObjectParser[] parsers;

    public XmlResponseParser(XmlObjectParser[] parsers) {
        this.parsers = parsers;
    }

    public T parse(final InputStream inStream) throws IOException {
        try {
            final XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inStream, null);
            xmlParser.nextTag();

            for (XmlObjectParser objectParser : parsers) {
                if (objectParser.checkObjectInXml(xmlParser)) {
                    return (T) objectParser.parse(xmlParser);
                }
            }
            throw new InvalidPropertiesFormatException("invalid input document");
        }
        catch (XmlPullParserException e) {
            throw new InvalidPropertiesFormatException(e);
        }
    }
}
