package com.object173.newsfeed.libs.network;

import java.io.IOException;
import java.io.InputStream;

public interface ResponseParser<T> {
    T parse(InputStream inStream) throws IOException;
}
