package com.object173.newsfeed.features.settings.domain;

import com.object173.newsfeed.features.settings.domain.model.AutoUpdateConfig;
import com.object173.newsfeed.features.settings.domain.model.NotificationConfig;

public interface SettingsRepository {
    void setAutoUpdateConfig(AutoUpdateConfig config);
    void setNotificationConfig(NotificationConfig config);
}
