package com.object173.newsfeed.features.settings.domain;

import android.content.Context;

public class SettingsInteractorImpl implements SettingsInteractor {

    private final AutoUpdateService mAutoUpdateService;

    public SettingsInteractorImpl(AutoUpdateService autoUpdateService) {
        mAutoUpdateService = autoUpdateService;
    }

    @Override
    public void changeAutoUpdateConfig(Context context) {
        mAutoUpdateService.start(context);
    }
}
