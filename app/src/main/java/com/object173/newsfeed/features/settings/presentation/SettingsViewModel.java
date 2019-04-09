package com.object173.newsfeed.features.settings.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.settings.domain.SettingsInteractor;

import androidx.lifecycle.ViewModel;

class SettingsViewModel extends ViewModel {

    private final SettingsInteractor mInteractor;

    SettingsViewModel(SettingsInteractor interactor) {
        mInteractor = interactor;
    }

    boolean changeAutoUpdateConfig(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = preferences.getBoolean(context.getString(R.string.pref_key_auto_refresh_enabled),
                context.getResources().getBoolean(R.bool.pref_auto_refresh_default));

        int updateInterval = preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                context.getResources().getInteger(R.integer.cache_frequency_default));

        boolean isWifiOnly = preferences.getBoolean(context.getString(R.string.pref_key_wifi_only),
                context.getResources().getBoolean(R.bool.pref_wifi_only_default));

        mInteractor.setAutoUpdateConfig(isEnabled, updateInterval, isWifiOnly);
        return true;
    }

    boolean changeNotificationConfig(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = preferences.getBoolean(context.getString(R.string.pref_key_notification_enabled),
                context.getResources().getBoolean(R.bool.pref_notification_enabled_default));

        String notificationTypeStr = preferences.getString(context.getString(R.string.pref_key_notification_type),
                context.getString(R.string.pref_notification_type_default));

        mInteractor.setNotificationConfig(isEnabled, Integer.parseInt(notificationTypeStr));
        return true;
    }
}
