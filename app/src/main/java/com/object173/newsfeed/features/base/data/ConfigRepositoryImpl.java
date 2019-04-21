package com.object173.newsfeed.features.base.data;

import com.object173.newsfeed.features.base.data.pref.PreferenceDataSource;
import com.object173.newsfeed.features.base.domain.ConfigRepository;
import com.object173.newsfeed.features.base.domain.model.pref.AutoUpdateConfig;
import com.object173.newsfeed.features.base.domain.model.pref.NotificationConfig;

public class ConfigRepositoryImpl implements ConfigRepository {

    private final PreferenceDataSource mDataSource;

    public ConfigRepositoryImpl(PreferenceDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public AutoUpdateConfig getAutoUpdateConfig() {
        return mDataSource.getAutoUpdateConfig();
    }

    @Override
    public NotificationConfig getNotificationConfig() {
        return mDataSource.getNotificationConfig();
    }
}
