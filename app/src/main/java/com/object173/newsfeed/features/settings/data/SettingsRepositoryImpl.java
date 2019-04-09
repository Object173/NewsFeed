package com.object173.newsfeed.features.settings.data;

import com.object173.newsfeed.features.settings.domain.SettingsRepository;
import com.object173.newsfeed.features.settings.domain.model.AutoUpdateConfig;
import com.object173.newsfeed.features.settings.domain.model.NotificationConfig;

public class SettingsRepositoryImpl implements SettingsRepository {

    @Override
    public void setAutoUpdateConfig(AutoUpdateConfig config) {
        AutoUpdateWorker.setConfig(config);
    }

    @Override
    public void setNotificationConfig(NotificationConfig config) {

    }
}
