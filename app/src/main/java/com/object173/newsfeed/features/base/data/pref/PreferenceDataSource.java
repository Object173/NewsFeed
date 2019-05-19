package com.object173.newsfeed.features.base.data.pref;

import androidx.annotation.StyleRes;

import com.object173.newsfeed.features.base.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.model.pref.CacheConfig;
import com.object173.newsfeed.features.base.model.pref.NotificationConfig;

public interface PreferenceDataSource {
    AutoUpdateConfig getAutoUpdateConfig();
    NotificationConfig getNotificationConfig();
    CacheConfig getCacheConfig();

    @StyleRes
    int getCurrentThemeId();

    @StyleRes
    int updateCurrentThemeId();
}
