package com.object173.newsfeed.features.settings.domain;

public interface SettingsInteractor {
    void setAutoUpdateConfig(boolean isEnabled, int frequency, boolean isWifiOnly);
    void setNotificationConfig(boolean isEnabled, int type);
}
