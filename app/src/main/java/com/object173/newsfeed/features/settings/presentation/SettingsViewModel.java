package com.object173.newsfeed.features.settings.presentation;

import android.content.Context;

import com.object173.newsfeed.features.settings.device.AutoUpdateWorker;
import com.object173.newsfeed.features.settings.domain.SettingsInteractor;

import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private final SettingsInteractor mInteractor;

    SettingsViewModel(SettingsInteractor interactor) {
        mInteractor = interactor;
    }

    void changeAutoUpdateConfig(Context context) {
        mInteractor.changeAutoUpdateConfig(context);
    }
}
