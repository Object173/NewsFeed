package com.object173.newsfeed.features.settings.domain;

import com.object173.newsfeed.features.settings.domain.model.AutoUpdateConfig;
import com.object173.newsfeed.features.settings.domain.model.NotificationConfig;

public class SettingsInteractorImpl implements SettingsInteractor {

    private final SettingsRepository mRepository;

    public SettingsInteractorImpl(SettingsRepository repository) {
        mRepository = repository;
    }

    @Override
    public void setAutoUpdateConfig(boolean isEnabled, int frequency, boolean isWifiOnly) {
        mRepository.setAutoUpdateConfig(new AutoUpdateConfig(isEnabled, frequency, isWifiOnly));
    }

    @Override
    public void setNotificationConfig(boolean isEnabled, int type) {
        mRepository.setNotificationConfig(new NotificationConfig(isEnabled, NotificationConfig.Type.values()[type]));
    }
}
