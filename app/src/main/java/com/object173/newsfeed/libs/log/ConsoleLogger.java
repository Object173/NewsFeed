package com.object173.newsfeed.libs.log;

import android.util.Log;

import androidx.annotation.NonNull;

final class ConsoleLogger implements ILogger {
    private final String tag;

    ConsoleLogger(@NonNull final String tag) {
        this.tag = tag;
    }

    @Override
    public void info(final String message, final Throwable throwable) {
        Log.i(tag, message, throwable);
    }

    @Override
    public void info(final String message) {
        Log.i(tag, message);
    }

    @Override
    public void warning(final String message, final Throwable throwable) {
        Log.w(tag, message, throwable);
    }

    @Override
    public void warning(final String message) {
        Log.w(tag, message);
    }

    @Override
    public void error(final String message, final Throwable throwable) {
        Log.e(tag, message, throwable);
    }

    @Override
    public void error(final String message) {
        Log.e(tag, message);
    }

    @Override
    public void verbose(final String message, final Throwable throwable) {
       Log.v(tag, message, throwable);
    }

    @Override
    public void verbose(final String message) {
        Log.v(tag, message);
    }

    @Override
    public void debug(final String message, final Throwable throwable) {
        Log.d(tag, message, throwable);
    }

    @Override
    public void debug(final String message) {
        Log.d(tag, message);
    }
}
