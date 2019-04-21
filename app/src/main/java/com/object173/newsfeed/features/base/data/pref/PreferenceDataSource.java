package com.object173.newsfeed.features.base.data.pref;

import com.object173.newsfeed.features.base.domain.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.domain.model.pref.CacheConfig;
import com.object173.newsfeed.features.base.domain.model.pref.NotificationConfig;

public interface PreferenceDataSource {
    AutoUpdateConfig getAutoUpdateConfig();
    NotificationConfig getNotificationConfig();
    CacheConfig getCacheConfig();
}
