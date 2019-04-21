package com.object173.newsfeed.features.base.domain;

import com.object173.newsfeed.features.base.domain.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.domain.model.pref.NotificationConfig;

public interface ConfigRepository {
    AutoUpdateConfig getAutoUpdateConfig();
    NotificationConfig getNotificationConfig();
}
