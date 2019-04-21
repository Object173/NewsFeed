package com.object173.newsfeed.features.base.domain.model.pref;

public class AutoUpdateConfig {

    public final boolean enabled;
    public final int updateInterval;
    public final boolean wifiOnly;

    public AutoUpdateConfig(boolean enabled, int updateInterval, boolean wifiOnly) {
        this.enabled = enabled;
        this.updateInterval = updateInterval;
        this.wifiOnly = wifiOnly;
    }
}
