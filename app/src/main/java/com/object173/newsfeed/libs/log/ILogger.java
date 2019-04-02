package com.object173.newsfeed.libs.log;

public interface ILogger {
    void info(String message, Throwable throwable);
    void info(String message);
    void warning(String message, Throwable throwable);
    void warning(String message);
    void error(String message, Throwable throwable);
    void error(String message);
    void verbose(String message, Throwable throwable);
    void verbose(String message);
    void debug(String message, Throwable throwable);
    void debug(String message);
}
