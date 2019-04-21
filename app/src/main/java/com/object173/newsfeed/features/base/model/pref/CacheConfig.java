package com.object173.newsfeed.features.base.model.pref;

public class CacheConfig {
    public final int cacheSize;
    public final int cacheFrequency;

    public CacheConfig(int cacheSize, int cacheFrequency) {
        this.cacheSize = cacheSize;
        this.cacheFrequency = cacheFrequency;
    }
}
