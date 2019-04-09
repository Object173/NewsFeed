package com.object173.newsfeed.features.settings.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.object173.newsfeed.R;
import com.object173.newsfeed.features.settings.domain.model.AutoUpdateConfig;
import com.object173.newsfeed.features.settings.domain.model.NotificationConfig;

public class PreferencesDataSourceImpl implements PreferencesDataSource {
    @Override
    public AutoUpdateConfig getAutoUpdateConfig(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = preferences.getBoolean(context.getString(R.string.pref_key_auto_refresh_enabled),
                context.getResources().getBoolean(R.bool.pref_auto_refresh_default));

        int updateInterval = preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                context.getResources().getInteger(R.integer.cache_frequency_default));

        boolean isWifiOnly = preferences.getBoolean(context.getString(R.string.pref_key_wifi_only),
                context.getResources().getBoolean(R.bool.pref_wifi_only_default));

        return new AutoUpdateConfig(isEnabled, updateInterval, isWifiOnly);
    }

    @Override
    public NotificationConfig getNotificationConfig(Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean isEnabled = preferences.getBoolean(context.getString(R.string.pref_key_notification_enabled),
                context.getResources().getBoolean(R.bool.pref_notification_enabled_default));

        String notificationTypeStr = preferences.getString(context.getString(R.string.pref_key_notification_type),
                context.getString(R.string.pref_notification_type_default));

        NotificationConfig.Type notificationType = NotificationConfig.Type
                .values()[Integer.parseInt(notificationTypeStr)];

        return new NotificationConfig(isEnabled, notificationType);
    }

    @Override
    public int getCacheSize(Context context) {
        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_key_cache_size),
                context.getResources().getInteger(R.integer.cache_size_default));
    }

    @Override
    public int getCacheFrequency(Context context) {
        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(context.getString(R.string.pref_key_cache_frequency),
                context.getResources().getInteger(R.integer.cache_frequency_default));
    }
}
