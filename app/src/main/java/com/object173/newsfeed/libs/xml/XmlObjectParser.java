package com.object173.newsfeed.libs.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface XmlObjectParser {
    boolean checkObjectInXml(XmlPullParser xmlParser) throws XmlPullParserException;
    Object parse(XmlPullParser xmlParser) throws IOException, XmlPullParserException;
}
