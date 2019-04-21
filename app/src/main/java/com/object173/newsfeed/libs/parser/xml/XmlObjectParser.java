package com.object173.newsfeed.libs.parser.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public interface XmlObjectParser {
    boolean checkObjectInXml(XmlPullParser xmlParser) throws XmlPullParserException;
    Object parse(XmlPullParser xmlParser) throws IOException, XmlPullParserException;
}
