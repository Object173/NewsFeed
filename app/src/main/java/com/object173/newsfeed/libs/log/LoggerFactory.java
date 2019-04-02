package com.object173.newsfeed.libs.log;

import com.object173.newsfeed.BuildConfig;

public final class LoggerFactory {

    private static final boolean LOG_ENABLED = BuildConfig.DEBUG;
    private static final EmptyLogger EMPTY_LOGGER = LOG_ENABLED ? null : new EmptyLogger();

    public static ILogger get(final Class type) {
        return LOG_ENABLED ? new ConsoleLogger(type.getName()) : EMPTY_LOGGER;
    }
}
