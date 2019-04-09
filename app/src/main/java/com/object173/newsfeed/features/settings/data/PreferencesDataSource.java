package com.object173.newsfeed.features.settings.data;

import android.content.Context;

import com.object173.newsfeed.features.settings.domain.model.AutoUpdateConfig;
import com.object173.newsfeed.features.settings.domain.model.NotificationConfig;

public interface PreferencesDataSource {
    AutoUpdateConfig getAutoUpdateConfig(Context context);
    NotificationConfig getNotificationConfig(Context context);

    int getCacheSize(Context context);
    int getCacheFrequency(Context context);
}
