package com.object173.newsfeed.features.settings.presentation;

import android.content.Context;

import com.object173.newsfeed.features.settings.device.AutoUpdateWorker;

import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    void changeAutoUpdateConfig(Context context) {
        AutoUpdateWorker.start(context);
    }
}
